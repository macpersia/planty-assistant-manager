package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.SkillRepository;
import be.planty.managers.assistant.repository.UserRepository;
import be.planty.managers.assistant.security.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
    public static final String HEADER_KEY_SIMP_SESSION_ID = "simpSessionId";

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
        logger.debug("On requestPayload from '" + skillSessionId + "' for '" + emailAddress + "' : " + requestPayload);
        final Optional<Agent> agent = agentRepo.findByEmailAddress(emailAddress).stream().findFirst();
        final String dest = "/queue/action-requests";

        final Optional<String> username = agent.map(a -> a.getUser().getLogin());
        final String prettyRequest = toPrettyString(requestPayload);
        logger.debug("Forwarding requestPayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyRequest);
        assert username.isPresent();

        //this.messagingTemplate.convertAndSendToUser(username.get(), dest, requestPayload, stompHeaderAccessor.getMessageHeaders());
        this.messagingTemplate.send("/user/" + encodeUsername(username) + dest, message);
    }

    private String encodeUsername(Optional<String> username) {
        return username.get().replaceAll("/", "%2F");
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
        final Optional<String> agentUsername = SecurityUtils.getCurrentUserLogin();
        final Optional<String> emailAddress = agentUsername.flatMap(userRepo::findOneByLogin).map(User::getEmail);
        final String dest = "/queue/action-responses/" + emailAddress.orElse(null);

        final Optional<String> username = agentUsername.flatMap(this.skillRepo::findSkillLoginMatchingAgentLogin);
        final String prettyResponse = toPrettyString(responsePayload);
        logger.debug("Forwarding responsePayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyResponse);
        assert username.isPresent();

        //this.messagingTemplate.convertAndSendToUser(username.get(), dest, responsePayload, headerAccessor.getMessageHeaders());
        this.messagingTemplate.send("/user/" + encodeUsername(username) + dest, message);
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
