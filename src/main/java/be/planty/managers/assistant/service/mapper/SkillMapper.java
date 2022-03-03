package be.planty.managers.assistant.service.mapper;

import be.planty.managers.assistant.domain.*;
import be.planty.managers.assistant.service.dto.SkillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Skill and its DTO SkillDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {



    default Skill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Skill skill = new Skill();
        skill.setId(id);
        return skill;
    }
}
