package be.planty.assistant.repository;

import be.planty.assistant.domain.Skill;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SkillRepositoryWithBagRelationshipsImpl implements SkillRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Skill> fetchBagRelationships(Optional<Skill> skill) {
        return skill.map(this::fetchUsers);
    }

    @Override
    public Page<Skill> fetchBagRelationships(Page<Skill> skills) {
        return new PageImpl<>(fetchBagRelationships(skills.getContent()), skills.getPageable(), skills.getTotalElements());
    }

    @Override
    public List<Skill> fetchBagRelationships(List<Skill> skills) {
        return Optional.of(skills).map(this::fetchUsers).get();
    }

    Skill fetchUsers(Skill result) {
        return entityManager
            .createQuery("select skill from Skill skill left join fetch skill.users where skill is :skill", Skill.class)
            .setParameter("skill", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Skill> fetchUsers(List<Skill> skills) {
        return entityManager
            .createQuery("select distinct skill from Skill skill left join fetch skill.users where skill in :skills", Skill.class)
            .setParameter("skills", skills)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
