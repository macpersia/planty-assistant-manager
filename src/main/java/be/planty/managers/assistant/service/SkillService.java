package be.planty.managers.assistant.service;

import be.planty.managers.assistant.domain.Skill;
import be.planty.managers.assistant.repository.SkillRepository;
import be.planty.managers.assistant.service.dto.SkillDTO;
import be.planty.managers.assistant.service.mapper.SkillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Skill.
 */
@Service
@Transactional
public class SkillService {

    private final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    public SkillService(SkillRepository skillRepository, SkillMapper skillMapper) {
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    /**
     * Save a skill.
     *
     * @param skillDTO the entity to save
     * @return the persisted entity
     */
    public SkillDTO save(SkillDTO skillDTO) {
        log.debug("Request to save Skill : {}", skillDTO);
        Skill skill = skillMapper.toEntity(skillDTO);
        skill = skillRepository.save(skill);
        return skillMapper.toDto(skill);
    }

    /**
     * Get all the skills.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<SkillDTO> findAll() {
        log.debug("Request to get all Skills");
        return skillRepository.findAllWithEagerRelationships().stream()
            .map(skillMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the Skill with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<SkillDTO> findAllWithEagerRelationships(Pageable pageable) {
        return skillRepository.findAllWithEagerRelationships(pageable).map(skillMapper::toDto);
    }
    

    /**
     * Get one skill by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<SkillDTO> findOne(Long id) {
        log.debug("Request to get Skill : {}", id);
        return skillRepository.findOneWithEagerRelationships(id)
            .map(skillMapper::toDto);
    }

    /**
     * Delete the skill by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Skill : {}", id);
        skillRepository.deleteById(id);
    }
}
