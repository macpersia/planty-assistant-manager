package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * Spring Data  repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("select agent from Agent agent where agent.user.login = ?#{principal.username}")
    List<Agent> findByUserIsCurrentUser();

    @Query("select a from Agent a where a.user.email = ?1 order by a.id desc")
    List<Agent> findByEmailAddress(String emailAddress); /*{
        final Agent agentEx = new Agent();
        final User userEx = new User();
        userEx.setEmail(emailAddress);
        agentEx.setUser(userEx);
        final List<Agent> agents = findAll(Example.of(agentEx), Sort.by(DESC, "id"));
        return agents.stream();
    }*/

    @Query("select a from Agent a where a.sessionId = ?1")
    Optional<Agent> findBySessionId(String sessionId);
}
