package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.PairingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PairingRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PairingRequestRepository extends MongoRepository<PairingRequest, String> {

}
