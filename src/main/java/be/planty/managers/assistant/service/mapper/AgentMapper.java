package be.planty.managers.assistant.service.mapper;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.service.dto.AgentDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Agent and its DTO AgentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {


}
