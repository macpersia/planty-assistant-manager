package be.planty.assistant.service;

import be.planty.assistant.domain.Agent;
import be.planty.assistant.repository.AgentRepository;
import be.planty.assistant.service.dto.AgentDTO;
import be.planty.assistant.service.mapper.AgentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Agent}.
 */
@Service
@Transactional
public class AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final AgentRepository agentRepository;

    private final AgentMapper agentMapper;

    public AgentService(AgentRepository agentRepository, AgentMapper agentMapper) {
        this.agentRepository = agentRepository;
        this.agentMapper = agentMapper;
    }

    /**
     * Save a agent.
     *
     * @param agentDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentDTO save(AgentDTO agentDTO) {
        log.debug("Request to save Agent : {}", agentDTO);
        Agent agent = agentMapper.toEntity(agentDTO);
        agent = agentRepository.save(agent);
        return agentMapper.toDto(agent);
    }

    /**
     * Partially update a agent.
     *
     * @param agentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AgentDTO> partialUpdate(AgentDTO agentDTO) {
        log.debug("Request to partially update Agent : {}", agentDTO);

        return agentRepository
            .findById(agentDTO.getId())
            .map(existingAgent -> {
                agentMapper.partialUpdate(existingAgent, agentDTO);

                return existingAgent;
            })
            .map(agentRepository::save)
            .map(agentMapper::toDto);
    }

    /**
     * Get all the agents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentDTO> findAll() {
        log.debug("Request to get all Agents");
        return agentRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(agentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the agents with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AgentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return agentRepository.findAllWithEagerRelationships(pageable).map(agentMapper::toDto);
    }

    /**
     * Get one agent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentDTO> findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findOneWithEagerRelationships(id).map(agentMapper::toDto);
    }

    /**
     * Delete the agent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.deleteById(id);
    }
}
