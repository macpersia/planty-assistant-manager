package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.service.AgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class MessagingService {

    private static final Logger log = LoggerFactory.getLogger(MessagingService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final AgentService agentSvc;

    public MessagingService(SimpMessageSendingOperations messagingTemplate, AgentService agentSvc) {
        this.messagingTemplate = messagingTemplate;
        this.agentSvc = agentSvc;
    }

//    @MessageMapping("/topic/action.req")
//    @SendTo("/topic/action.req")
//    public Object onRequest(@Payload Object request, StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
//        log.debug("On request {}", request);
//        return request;
//    }
//
//    @MessageMapping("/topic/action.res")
//    @SendTo("/topic/action.res")
//    public Object onResponse(@Payload Object response, StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
//        log.debug("On response {}", response);
//        return response;
//    }

}
