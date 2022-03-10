package be.planty.assistant.repository;

import be.planty.assistant.domain.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Skill entity.
 */
@Repository
public interface SkillRepository 
                extends SkillRepositoryWithBagRelationships, JpaRepository<Skill, Long> {

    // Commented by Hadi, when migrating to JHipster 7.7.0
    // @Query("select skill from Skill skill left join fetch skill.users where skill.id = ?1")
    default Optional<Skill> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

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

    default List<Skill> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Skill> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

}
