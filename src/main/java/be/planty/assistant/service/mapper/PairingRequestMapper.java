package be.planty.assistant.service.mapper;

import be.planty.assistant.domain.PairingRequest;
import be.planty.assistant.service.dto.PairingRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PairingRequest} and its DTO {@link PairingRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface PairingRequestMapper extends EntityMapper<PairingRequestDTO, PairingRequest> {}
