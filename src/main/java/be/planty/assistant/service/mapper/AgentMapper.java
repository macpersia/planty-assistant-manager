package be.planty.assistant.service.mapper;

import be.planty.assistant.domain.Agent;
import be.planty.assistant.service.dto.AgentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    AgentDTO toDto(Agent s);
}
