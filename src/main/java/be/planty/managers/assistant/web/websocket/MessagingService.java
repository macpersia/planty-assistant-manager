package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.Skill;
import be.planty.managers.assistant.domain.User;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.SkillRepository;
import be.planty.managers.assistant.repository.UserRepository;
import be.planty.managers.assistant.security.SecurityUtils;
import be.planty.models.assistant.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

import static be.planty.models.assistant.Constants.ORIGIN_EMAIL_KEY;
import static java.lang.Boolean.TRUE;
import static java.util.Optional.ofNullable;
import static org.springframework.messaging.support.NativeMessageHeaderAccessor.getFirstNativeHeader;

@Controller
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    public static final String HEADER_KEY_SIMP_SESSION_ID = "simpSessionId";
    public static final String HEADER_KEY_PLANTY_ORIGIN_EMAIL = "planty.origin.email";

    private final SimpMessageSendingOperations messagingTemplate;

    private final AgentRepository agentRepo;
    private final UserRepository userRepo;
    private final SkillRepository skillRepo;

    public MessagingService(SimpMessageSendingOperations messagingTemplate,
                            AgentRepository agentRepo, UserRepository userRepo,
                            SkillRepository skillRepo) {
        this.messagingTemplate = messagingTemplate;
        this.agentRepo = agentRepo;
        this.userRepo = userRepo;
        this.skillRepo = skillRepo;
    }

    @MessageMapping("/topic/action-requests/{emailAddress}")
    //@SendTo("/topic/action.req")
    public void onRequest(//@Payload final ActionRequest requestPayload,
                          //final StompHeaderAccessor stompHeaderAccessor, final Principal principal
                          final Message message, @DestinationVariable final String emailAddress) {

        final Object requestPayload = message.getPayload();
        //final String skillSessionId = stompHeaderAccessor.getSessionId();
        final String skillSessionId = message.getHeaders().get(HEADER_KEY_SIMP_SESSION_ID, String.class);
        final Optional<String> skillUsername = SecurityUtils.getCurrentUserLogin();
        logger.debug("On requestPayload from '" + skillSessionId + "' (" + skillUsername.orElse(null) + ")"
			+ " for '" + emailAddress + "' : " + requestPayload);

        final Skill skill = skillUsername.flatMap(skillRepo::findOneWithEagerRelationships).get();
        logger.debug("Here's the list of skill users...");
        for (User u : skill.getUsers()) logger.debug("\t" + u.getLogin());

        final List<Agent> filteredAgents = TRUE.equals(skill.isAgentSharing()) ?
            agentRepo.findTop10SkillAgentsLatestFirst(skill.getUsers())
            : agentRepo.findTop10ByEmailAddressLatestFirst(emailAddress);

        logger.debug("Here's the list of filtered agents...");
        for (Agent a : filteredAgents) logger.debug("\t" + a.getUser().getLogin());

        final Optional<String> username =  filteredAgents.stream()
            .findFirst().map(a -> a.getUser().getLogin());

        final String dest = "/queue/action-requests";

        final String prettyRequest = toPrettyString(requestPayload);
        logger.debug("Forwarding requestPayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyRequest);
        assert username.isPresent();

        //this.messagingTemplate.convertAndSendToUser(username.get(), dest, requestPayload, stompHeaderAccessor.getMessageHeaders());
        this.messagingTemplate.send("/user/" + encodeUsername(username) + dest, message);
    }

    @MessageMapping("/topic/action-responses")
    //@SendTo("/topic/action.res")
    public void onResponse(//@Payload final ActionResponse responsePayload,
                           //final StompHeaderAccessor headerAccessor, /*final Principal principal*/
                           final Message message) {

        final Object responsePayload = message.getPayload();
        //final String agentSessionId = headerAccessor.getSessionId();
        final String agentSessionId = message.getHeaders().get(HEADER_KEY_SIMP_SESSION_ID, String.class);
        logger.debug("On responsePayload from '" + agentSessionId + "' : " + responsePayload);
        //logger.info("\theaders...");
        //message.getHeaders().forEach((k, v) -> logger.info("\t" + k + ": " + v));
        final Optional<String> agentUsername = SecurityUtils.getCurrentUserLogin();

        final Skill skill = agentUsername.flatMap(skillRepo::findOneWithEagerRelationships).get();
        logger.debug("Here's the list of skill users...");
        for (User u : skill.getUsers()) logger.debug("\t" + u.getLogin());

        final Optional<String> emailAddress = TRUE.equals(skill.isAgentSharing()) ?
            ofNullable(getFirstNativeHeader(ORIGIN_EMAIL_KEY, message.getHeaders()))
            : agentUsername.flatMap(userRepo::findOneByLogin).map(User::getEmail);

        final String dest = "/queue/action-responses/" + emailAddress.orElse(null);

        final Optional<String> username = agentUsername.flatMap(this.skillRepo::findSkillLoginMatchingAgentLogin);
        final String prettyResponse = toPrettyString(responsePayload);
        logger.debug("Forwarding responsePayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyResponse);
        assert username.isPresent();

        //this.messagingTemplate.convertAndSendToUser(username.get(), dest, responsePayload, headerAccessor.getMessageHeaders());
        this.messagingTemplate.send("/user/" + encodeUsername(username) + dest, message);
    }

    private String encodeUsername(Optional<String> username) {
        return username.get().replaceAll("/", "%2F");
    }

    private String toPrettyString(Object payload) {
        String prettyPayload;
        try {
            prettyPayload = payload instanceof String ?
                (String) payload
                : (payload instanceof byte[] ?
                    new String((byte[]) payload)
                    : objectWriter.writeValueAsString(payload));

        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            prettyPayload = String.valueOf(payload);
        }
        return prettyPayload;
    }
}
