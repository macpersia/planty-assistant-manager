package be.planty.assistant.service.mapper;

import be.planty.assistant.domain.Agent;
import be.planty.assistant.domain.User;
import be.planty.assistant.service.dto.AgentDTO;
import be.planty.assistant.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userLogin")
    AgentDTO toDto(Agent s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
