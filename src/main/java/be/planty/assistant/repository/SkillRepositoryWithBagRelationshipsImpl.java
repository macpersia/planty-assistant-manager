package be.planty.assistant.repository;

import be.planty.assistant.domain.Skill;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class SkillRepositoryWithBagRelationshipsImpl implements SkillRepositoryWithBagRelationships {

    @PersistenceContext
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
        return Optional.of(skills).map(this::fetchUsers).orElse(Collections.emptyList());
    }

    Skill fetchUsers(Skill result) {
        return entityManager
            .createQuery("select skill from Skill skill left join fetch skill.users where skill is :skill", Skill.class)
            .setParameter("skill", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Skill> fetchUsers(List<Skill> skills) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, skills.size()).forEach(index -> order.put(skills.get(index).getId(), index));
        List<Skill> result = entityManager
            .createQuery("select distinct skill from Skill skill left join fetch skill.users where skill in :skills", Skill.class)
            .setParameter("skills", skills)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
