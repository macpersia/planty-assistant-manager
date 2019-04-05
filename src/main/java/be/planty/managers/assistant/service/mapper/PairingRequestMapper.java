package be.planty.managers.assistant.service.mapper;

import be.planty.managers.assistant.domain.*;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PairingRequest and its DTO PairingRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PairingRequestMapper extends EntityMapper<PairingRequestDTO, PairingRequest> {



    default PairingRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        PairingRequest pairingRequest = new PairingRequest();
        pairingRequest.setId(id);
        return pairingRequest;
    }
}
