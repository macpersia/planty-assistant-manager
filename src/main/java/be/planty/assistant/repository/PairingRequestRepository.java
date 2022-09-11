package be.planty.assistant.repository;

import be.planty.assistant.domain.PairingRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PairingRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PairingRequestRepository extends JpaRepository<PairingRequest, Long> {}
