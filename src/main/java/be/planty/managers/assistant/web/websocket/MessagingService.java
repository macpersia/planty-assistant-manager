package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.SkillRepository;
import be.planty.managers.assistant.repository.UserRepository;
import be.planty.managers.assistant.security.SecurityUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

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
    public void onRequest(@Payload Object requestPayload, @DestinationVariable String emailAddress,
                          StompHeaderAccessor headerAccessor/*, Principal principal*/) {
        logger.debug("On requestPayload from '" + headerAccessor.getSessionId() + "' for '" + emailAddress + "' : " + requestPayload);
        final Optional<Agent> agent = agentRepo.findByEmailAddress(emailAddress).stream().findFirst();
        final String dest = "/queue/action-requests";
        final Optional<String> username = agent.map(a -> a.getUser().getLogin());
        final String prettyRequest = toPrettyString(requestPayload);
        logger.debug("Forwarding requestPayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyRequest);
        assert username.isPresent();
        this.messagingTemplate.convertAndSendToUser(username.get(), dest, requestPayload, headerAccessor.getMessageHeaders());
    }

    @MessageMapping("/topic/action-responses")
    //@SendTo("/topic/action.res")
    public void onResponse(@Payload Object responsePayload,
                           StompHeaderAccessor headerAccessor/*, Principal principal*/) {
        final String agentSessionId = headerAccessor.getSessionId();
        logger.debug("On responsePayload from '" + agentSessionId + "' : " + responsePayload);
        final Optional<String> agentUsername = SecurityUtils.getCurrentUserLogin();
        final Optional<String> emailAddress = agentUsername.flatMap(userRepo::findOneByLogin).map(User::getEmail);
        final String dest = "/queue/action-responses/" + emailAddress.orElse(null);

        final Optional<String> username = agentUsername.flatMap(this.skillRepo::findSkillLoginMatchingAgentLogin);
        final String prettyResponse = toPrettyString(responsePayload);
        logger.debug("Forwarding responsePayload to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyResponse);
        assert username.isPresent();
        this.messagingTemplate.convertAndSendToUser(username.get(), dest, responsePayload, headerAccessor.getMessageHeaders());
    }

    private String toPrettyString(Object payload) {
        String prettyPayload;
        try {
            prettyPayload = payload instanceof String ?
                (String) payload
                : new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            prettyPayload = String.valueOf(payload);
        }
        return prettyPayload;
    }

}
