package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.UserRepository;
import be.planty.managers.assistant.security.SecurityUtils;
import be.planty.managers.assistant.service.AgentService;
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

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
public class MessagingService {

    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);

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

    @MessageMapping("/topic/action.req/{emailAddress}")
    //@SendTo("/topic/action.req")
    public void onRequest(@Payload Object request, @DestinationVariable String emailAddress,
                          StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
        log.debug("On request from " + emailAddress + " : " + request);
        final Optional<String> agentSessionId =
            agentRepo.findByEmailAddress(emailAddress).stream()
            .map(Agent::getSessionId).findFirst();

        final String dest = "/topic/action.req";
        log.debug("Forwarding request to '" + dest + "' : '" + agentSessionId  + "' : " + request);
        assert agentSessionId.isPresent();
        this.messagingTemplate.convertAndSendToUser(agentSessionId.orElse(null), dest, request);
    }

    @MessageMapping("/topic/action.res")
    //@SendTo("/topic/action.res")
    public void onResponse(@Payload Object response,
                           StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
        final String agentSessionId = stompHeaderAccessor.getSessionId();
        log.debug("On response from '" + agentSessionId + "' : " + response);
        final Optional<String> agentLogin = SecurityUtils.getCurrentUserLogin();
        final Optional<User> emailAddress = agentLogin.flatMap(userRepo::findOneByLogin);
        final String dest = "/topic/action.res/" + emailAddress.orElse(null);
        log.debug("Forwarding response to '" + dest + "' : " + response);
        this.messagingTemplate.convertAndSend(dest, response);
    }

}
