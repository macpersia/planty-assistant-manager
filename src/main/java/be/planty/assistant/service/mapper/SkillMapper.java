package be.planty.assistant.service.mapper;

import be.planty.assistant.domain.Skill;
import be.planty.assistant.service.dto.SkillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    @Mapping(target = "users", source = "users", qualifiedByName = "loginSet")
    SkillDTO toDto(Skill s);

    @Mapping(target = "removeUsers", ignore = true)
    Skill toEntity(SkillDTO skillDTO);
}
