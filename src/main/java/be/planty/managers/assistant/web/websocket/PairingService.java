package be.planty.managers.assistant.web.websocket;

import be.planty.managers.assistant.security.SecurityUtils;
import be.planty.managers.assistant.service.PairingRequestService;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Optional;
import java.util.concurrent.Executors;

@Controller
public class PairingService implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger log = LoggerFactory.getLogger(PairingService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final PairingRequestService pairingRequestSvc;

    public PairingService(SimpMessageSendingOperations messagingTemplate, PairingRequestService pairingRequestSvc) {
        this.messagingTemplate = messagingTemplate;
        this.pairingRequestSvc = pairingRequestSvc;
    }

    @MessageMapping("/topic/pairing-requests")
    //@SendTo("/topic/pairing.res")
    public void onPairing(@Payload PairingRequestDTO dto, StompHeaderAccessor stompHeaderAccessor/*, Principal principal*/) {
        final String sessionId = stompHeaderAccessor.getSessionId();
        dto.setSessionId(sessionId);
        final Optional<String> login = SecurityUtils.getCurrentUserLogin();
        login.ifPresent(dto::setLogin);
        log.debug("Saving pairing request {}", dto);
        pairingRequestSvc.setMessageTemplate(this.messagingTemplate);
        final PairingRequestDTO savedDto = pairingRequestSvc.save(dto);
        final String response = "Your request is pending approval...";
        assert login.isPresent();
        this.messagingTemplate.convertAndSendToUser(login.orElse(null),"/queue/pairing-responses", response);

        // TODO: The following is a temporary hack to bypass pairing approval step.
        Executors.newSingleThreadExecutor().submit(() -> {
            savedDto.setAccepted(true);
            pairingRequestSvc.save(savedDto);
        });
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        log.debug("On SessionSubscribeEvent: {}", event);
        //messagingTemplate.convertAndSend("/topic/pairing/responses", "Your subscription received.");
    }
}
