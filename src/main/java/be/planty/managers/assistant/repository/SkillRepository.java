package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Skill entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    @Query(value = "select distinct skill from Skill skill left join fetch skill.users",
        countQuery = "select count(distinct skill) from Skill skill")
    Page<Skill> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct skill from Skill skill left join fetch skill.users")
    List<Skill> findAllWithEagerRelationships();

    @Query("select skill from Skill skill left join fetch skill.users where skill.id = ?1")
    Optional<Skill> findOneWithEagerRelationships(Long id);

    @Query(
        "select distinct u2.login from Skill s" +
        " left join s.users u1 left join u1.authorities au1" +
        " left join s.users u2 left join u2.authorities au2" +
        " where au1.name = 'ROLE_AGENT'" +
            " and au2.name = 'ROLE_SKILL'" +
            " and u1.login = ?1")
    Optional<String> findSkillLoginMatchingAgentLogin(String agentLogin);

    @Query(
        "select distinct s from Skill s" +
        " left join fetch s.users tu left join fetch tu.authorities" +
	    " left join fetch s.users ou left join fetch ou.authorities" +
	    " where tu.login = :targetLogin")
    Optional<Skill> findOneWithEagerRelationships(@Param("targetLogin") String targetLogin);
}
