package be.planty.managers.assistant.service;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.PairingRequest;
import be.planty.managers.assistant.repository.AgentRepository;
import be.planty.managers.assistant.repository.PairingRequestRepository;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
import be.planty.managers.assistant.service.mapper.PairingRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PairingRequest.
 */
@Service
public class PairingRequestService {

    private final Logger log = LoggerFactory.getLogger(PairingRequestService.class);

    private final PairingRequestRepository pairingRequestRepository;
    private final AgentRepository agentRepository;

    private final PairingRequestMapper pairingRequestMapper;

    private SimpMessageSendingOperations messageTemplate;

    public PairingRequestService(PairingRequestRepository pairingRequestRepository,
                                 AgentRepository agentRepository,
                                 PairingRequestMapper pairingRequestMapper
    ) {
        this.pairingRequestRepository = pairingRequestRepository;
        this.agentRepository = agentRepository;
        this.pairingRequestMapper = pairingRequestMapper;
    }

    /**
     * Save a pairingRequest.
     *
     * @param dto the entity to save
     * @return the persisted entity
     */
    public PairingRequestDTO save(PairingRequestDTO dto) {
        if (dto.isAccepted() != null) {
            // TODO: Additional security check to ensure only the same skill customer can update it.
        } else {
            dto.setAccepted(false);
        }
        dto.setRequestTime(ZonedDateTime.now());
        log.debug("Request to save PairingRequest : {}", dto);
        PairingRequest pairingReq = pairingRequestMapper.toEntity(dto);
        pairingReq = pairingRequestRepository.save(pairingReq);
        if (pairingReq.isAccepted()) {
            Agent agent = new Agent();
            final String sessionId = dto.getId();
            agent.setSessionId(sessionId);
            agent.setName(dto.getName());
            agent.setPublicKey(dto.getPublicKey());
            agentRepository.save(agent);
            //if (this.messageTemplate != null) {
                assert this.messageTemplate != null;
                sendAccept(sessionId);
            //}
        }
        return pairingRequestMapper.toDto(pairingReq);
    }

    private void sendAccept(String sessionId) {
//        log.info("Sending 'accepted' to " + sessionId);
//        final SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(MESSAGE);
//        headerAccessor.setSessionId(sessionId);
//        headerAccessor.setLeaveMutable(true);
//        final MessageHeaders headers = headerAccessor.getMessageHeaders();
//        this.messageTemplate.convertAndSendToUser(sessionId, "/topic/pairing.res", "accepted", headers,
        log.info("Sending 'accepted' to /topic/pairing.res...");
        this.messageTemplate.convertAndSend("/topic/pairing.res", "accepted");
    }

    /**
     * Get all the pairingRequests.
     *
     * @return the list of entities
     */
    public List<PairingRequestDTO> findAll() {
        log.debug("Request to get all PairingRequests");
        return pairingRequestRepository.findAll().stream()
            .map(pairingRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pairingRequest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    public Optional<PairingRequestDTO> findOne(String id) {
        log.debug("Request to get PairingRequest : {}", id);
        return pairingRequestRepository.findById(id)
            .map(pairingRequestMapper::toDto);
    }

    /**
     * Delete the pairingRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(String id) {
        log.debug("Request to delete PairingRequest : {}", id);
        pairingRequestRepository.deleteById(id);
    }

    public void setMessageTemplate(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
}
