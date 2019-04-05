package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.PairingRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PairingRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PairingRequestRepository extends JpaRepository<PairingRequest, Long> {

}
