package be.planty.managers.assistant.service.mapper;

import be.planty.managers.assistant.domain.PairingRequest;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PairingRequest and its DTO PairingRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PairingRequestMapper extends EntityMapper<PairingRequestDTO, PairingRequest> {


}
