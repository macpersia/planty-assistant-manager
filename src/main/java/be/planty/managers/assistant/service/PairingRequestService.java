package be.planty.managers.assistant.service;

import be.planty.managers.assistant.domain.PairingRequest;
import be.planty.managers.assistant.repository.PairingRequestRepository;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
import be.planty.managers.assistant.service.mapper.PairingRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * Service Implementation for managing PairingRequest.
 */
@Service
@Transactional
public class PairingRequestService {

    private final Logger log = LoggerFactory.getLogger(PairingRequestService.class);

    private final PairingRequestRepository pairingRequestRepository;

    private final PairingRequestMapper pairingRequestMapper;

    public PairingRequestService(PairingRequestRepository pairingRequestRepository, PairingRequestMapper pairingRequestMapper) {
        this.pairingRequestRepository = pairingRequestRepository;
        this.pairingRequestMapper = pairingRequestMapper;
    }

    /**
     * Save a pairingRequest.
     *
     * @param pairingRequestDTO the entity to save
     * @return the persisted entity
     */
    public PairingRequestDTO save(PairingRequestDTO pairingRequestDTO) {
        log.debug("Request to save PairingRequest : {}", pairingRequestDTO);
        PairingRequest pairingRequest = pairingRequestMapper.toEntity(pairingRequestDTO);
        pairingRequest = pairingRequestRepository.save(pairingRequest);
        return pairingRequestMapper.toDto(pairingRequest);
    }

    /**
     * Get all the pairingRequests.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Optional<PairingRequestDTO> findOne(Long id) {
        log.debug("Request to get PairingRequest : {}", id);
        return pairingRequestRepository.findById(id)
            .map(pairingRequestMapper::toDto);
    }

    /**
     * Delete the pairingRequest by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PairingRequest : {}", id);
        pairingRequestRepository.deleteById(id);
    }
}
