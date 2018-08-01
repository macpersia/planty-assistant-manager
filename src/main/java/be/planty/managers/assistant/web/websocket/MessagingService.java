package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.UserRepository;
import be.planty.managers.assistant.security.SecurityUtils;
import be.planty.managers.assistant.service.AgentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final AgentService agentSvc;
    private final AgentRepository agentRepo;
    private final UserRepository userRepo;

    public MessagingService(SimpMessageSendingOperations messagingTemplate,
                            AgentService agentSvc,
                            AgentRepository agentRepo, UserRepository userRepo) {
        this.messagingTemplate = messagingTemplate;
        this.agentSvc = agentSvc;
        this.agentRepo = agentRepo;
        this.userRepo = userRepo;
    }

    //@MessageMapping("/topic/action.req/{emailAddress}")
    @MessageMapping("/topic/action-requests/{emailAddress}")
    //@SendTo("/topic/action.req")
    public void onRequest(@Payload Object request, @DestinationVariable String emailAddress,
                          StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
        // TODO: remove this!
        emailAddress = "agent.prototyper@localhost";
        logger.debug("On request from " + emailAddress + " : " + request);
        final Optional<Agent> agent = agentRepo.findByEmailAddress(emailAddress).stream().findFirst();
        final Optional<String> agentSessionId = agent.map(Agent::getSessionId);

        final String dest = "/queue/action-requests";
        final String destSessionId = agentSessionId.orElse(null);

        assert agentSessionId.isPresent();
        //log.debug("Forwarding request to '" + dest + "' : '" + destSessionId + "' : " + request);
        //this.messagingTemplate.convertAndSendToUser(destSessionId, dest, request);

        final Optional<String> username = agent.map(a -> a.getUser().getLogin());
        assert username.isPresent();
        String prettyRequest;
        try {
            prettyRequest = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            prettyRequest = String.valueOf(request);
        }
        logger.debug("Forwarding request to '" + username.orElse(null) + "' : '" + dest + "' : " + prettyRequest);
        this.messagingTemplate.convertAndSendToUser(username.orElse(null), dest, request);
    }

    @MessageMapping("/topic/action-responses")
    //@SendTo("/topic/action.res")
    public void onResponse(@Payload Object response,
                           StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
        final String agentSessionId = stompHeaderAccessor.getSessionId();
        logger.debug("On response from '" + agentSessionId + "' : " + response);
        final Optional<String> agentUsername = SecurityUtils.getCurrentUserLogin();
        // TODO: revert this!
        //final Optional<String> emailAddress = agentUsername.flatMap(userRepo::findOneByLogin).map(User::getEmail);
        final Optional<String> emailAddress = of("agent.prototyper");

        final String dest = "/queue/action-responses/" + emailAddress.orElse(null);
        //log.debug("Forwarding response to '" + dest + "' : " + response);
        //this.messagingTemplate.convertAndSend(dest, response);
        // TODO: no hard-coding!
        final Optional<String> username = of("skill.prototyper");
        logger.debug("Forwarding response to '" + username.orElse(null) + "' : '" + dest + "' : " + response);
        this.messagingTemplate.convertAndSendToUser(username.orElse(null), dest, response);
    }

}
