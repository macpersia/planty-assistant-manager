package be.planty.managers.assistant.service.mapper;

import be.planty.managers.assistant.domain.*;
import be.planty.managers.assistant.service.dto.AgentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Agent and its DTO AgentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {



    default Agent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agent agent = new Agent();
        agent.setId(id);
        return agent;
    }
}
