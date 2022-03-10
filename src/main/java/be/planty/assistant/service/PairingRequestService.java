package be.planty.assistant.service;

import be.planty.assistant.domain.Agent;
import be.planty.assistant.domain.PairingRequest;
import be.planty.assistant.domain.User;
import be.planty.assistant.repository.AgentRepository;
import be.planty.assistant.repository.PairingRequestRepository;
import be.planty.assistant.repository.UserRepository;
import be.planty.assistant.service.dto.PairingRequestDTO;
import be.planty.assistant.service.mapper.PairingRequestMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PairingRequest}.
 */
@Service
@Transactional
public class PairingRequestService {

    private final Logger log = LoggerFactory.getLogger(PairingRequestService.class);

    private final PairingRequestRepository pairingRequestRepository;

    private final AgentRepository agentRepository;

    private final PairingRequestMapper pairingRequestMapper;

    private final UserRepository userRepository;

    private SimpMessageSendingOperations messageTemplate;

    public PairingRequestService(PairingRequestRepository pairingRequestRepository,
                                 AgentRepository agentRepository,
                                 PairingRequestMapper pairingRequestMapper,
                                 UserRepository userRepository) {
        this.pairingRequestRepository = pairingRequestRepository;
        this.agentRepository = agentRepository;
        this.pairingRequestMapper = pairingRequestMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a pairingRequest.
     *
     * @param dto the entity to save.
     * @return the persisted entity.
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
            final Optional<User> user = userRepository.findOneByLogin(dto.getLogin());
            user.ifPresent(agent::setUser);
            final String sessionId = String.valueOf(dto.getSessionId());
            agent.setSessionId(sessionId);
            agent.setName(dto.getName());
            agent.setPublicKey(dto.getPublicKey());
            agentRepository.save(agent);
            //if (this.messageTemplate != null) {
                sendAccept(sessionId);
            //}
        }
        return pairingRequestMapper.toDto(pairingReq);
    }

    private void sendAccept(String sessionId) {
        final String dest = "/queue/pairing-responses";
        log.info("Sending 'accepted' to " + dest + "...");
        final Optional<String> username = agentRepository.findBySessionId(sessionId).map(a -> a.getUser().getLogin());
        assert username.isPresent();
        this.messageTemplate.convertAndSendToUser(username.orElse(null), dest, "accepted");
    }

    /**
     * Partially update a pairingRequest.
     *
     * @param pairingRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PairingRequestDTO> partialUpdate(PairingRequestDTO pairingRequestDTO) {
        log.debug("Request to partially update PairingRequest : {}", pairingRequestDTO);

        return pairingRequestRepository
            .findById(pairingRequestDTO.getId())
            .map(existingPairingRequest -> {
                pairingRequestMapper.partialUpdate(existingPairingRequest, pairingRequestDTO);

                return existingPairingRequest;
            })
            .map(pairingRequestRepository::save)
            .map(pairingRequestMapper::toDto);
    }

    /**
     * Get all the pairingRequests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PairingRequestDTO> findAll() {
        log.debug("Request to get all PairingRequests");
        return pairingRequestRepository
            .findAll()
            .stream()
            .map(pairingRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one pairingRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PairingRequestDTO> findOne(Long id) {
        log.debug("Request to get PairingRequest : {}", id);
        return pairingRequestRepository.findById(id).map(pairingRequestMapper::toDto);
    }

    /**
     * Delete the pairingRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PairingRequest : {}", id);
        pairingRequestRepository.deleteById(id);
    }

    public void setMessageTemplate(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }
}
