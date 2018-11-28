package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.Agent;
import be.planty.managers.assistant.domain.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    @Query("select agent from Agent agent where agent.user.login = ?#{principal.username}")
    List<Agent> findByUserIsCurrentUser();

    @Query("select a from Agent a where a.user.email = ?1 order by a.id desc")
    List<Agent> findByEmailAddressLatestFirst(String emailAddress, Pageable pageable); /*{
        final Agent agentEx = new Agent();
        final User userEx = new User();
        userEx.setEmail(emailAddress);
        agentEx.setUser(userEx);
        final List<Agent> agents = findAll(Example.of(agentEx), Sort.by(DESC, "id"));
        return agents.stream();
    }*/
    default List<Agent> findTop10ByEmailAddressLatestFirst(String emailAddress) {
        return this.findByEmailAddressLatestFirst(emailAddress, PageRequest.of(0, 10));
    }

    @Query("select a from Agent a where a.sessionId = ?1")
    Optional<Agent> findBySessionId(String sessionId);

    @Query("select distinct a from Agent a join a.user au where au in ?1 order by a.id desc")
    List<Agent> findSkillAgentsLatestFirst(Collection<User> skillUsers, Pageable pageable);

    default List<Agent> findTop10SkillAgentsLatestFirst(Collection<User> skillUsers) {
        return this.findSkillAgentsLatestFirst(skillUsers, PageRequest.of(0, 10));
    }
}
