package be.planty.assistant.service.mapper;

import be.planty.assistant.domain.Skill;
import be.planty.assistant.domain.User;
import be.planty.assistant.service.dto.SkillDTO;
import be.planty.assistant.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Skill} and its DTO {@link SkillDTO}.
 */
@Mapper(componentModel = "spring")
public interface SkillMapper extends EntityMapper<SkillDTO, Skill> {
    
    @Mapping(target = "users", source = "users", qualifiedByName = "userLoginSet")
    SkillDTO toDto(Skill s);

    // Commented by Hadi
    // @Mapping(target = "removeUsers", ignore = true)
    // Skill toEntity(SkillDTO skillDTO);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("userLoginSet")
    default Set<UserDTO> toDtoUserLoginSet(Set<User> user) {
        return user.stream().map(this::toDtoUserLogin).collect(Collectors.toSet());
    }
}
