package be.planty.managers.assistant.repository;

import be.planty.managers.assistant.domain.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
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

    @Query("select skill from Skill skill left join fetch skill.users where skill.id =:id")
    Optional<Skill> findOneWithEagerRelationships(@Param("id") Long id);

}
