package be.planty.assistant.repository;

import be.planty.assistant.domain.Agent;
import be.planty.assistant.domain.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Agent entity.
 */
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query("select a from Agent a where a.user.login = ?#{principal.username}")
    List<Agent> findByUserIsCurrentUser();

    default Optional<Agent> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Agent> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Agent> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct agent from Agent agent left join fetch agent.user",
        countQuery = "select count(distinct agent) from Agent agent"
    )
    Page<Agent> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct agent from Agent agent left join fetch agent.user")
    List<Agent> findAllWithToOneRelationships();

    @Query("select agent from Agent agent left join fetch agent.user where agent.id =:id")
    Optional<Agent> findOneWithToOneRelationships(@Param("id") Long id);

    @Query("select a from Agent a where a.user.email = ?1 order by a.id desc")
    List<Agent> findByEmailAddressLatestFirst(String emailAddress, Pageable pageable);/*{
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

    @Query("select distinct a from Agent a where a.user in ?1 order by a.id desc")
    List<Agent> findSkillAgentsLatestFirst(Collection<User> skillUsers, Pageable pageable);

    default List<Agent> findTop10SkillAgentsLatestFirst(Collection<User> skillUsers) {
        return this.findSkillAgentsLatestFirst(skillUsers, PageRequest.of(0, 10));
    }

    @Query("select distinct a from Agent a where a.user in ?1 and a.user.email = ?2 order by a.id desc")
    List<Agent> findSkillAgentsByEmailAddressLatestFirst(Collection<User> skillUsers, String emailAddress, Pageable pageable);

    default List<Agent> findTop10SkillAgentsByEmailAddressLatestFirst(Collection<User> skillUsers, String emailAddress) {
        return this.findSkillAgentsByEmailAddressLatestFirst(skillUsers, emailAddress, PageRequest.of(0, 10));
    }
}
