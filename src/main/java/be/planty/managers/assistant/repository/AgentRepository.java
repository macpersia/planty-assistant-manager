package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.Agent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("select agent from Agent agent where agent.user.login = ?#{principal.username}")
    List<Agent> findByUserIsCurrentUser();

}
