package com.telematic.spark.dao;

import com.telematic.spark.model.Member;
import com.telematic.spark.model.Member_;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * The example of a repository for the Member entity (the package is a symbolic
 * reference to the persistence layer.
 *
 * @author lcestari
 */
@ApplicationScoped
public class MemberRepository {

    @Inject
    private EntityManager em;

    public List<Member> findAllOrderedByName()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        return em.createQuery(criteria).getResultList();
    }

    public Member findByEmail(String email)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public Member findById(Long id)
    {
        return em.find(Member.class, id);
    }

    public Member findTheNewest()
    {
        return (Member) em.createQuery("SELECT m FROM Member m WHERE m.createdOn = (SELECT MAX(mm.createdOn) FROM Member mm WHERE mm.id = m.id)").getSingleResult();
    }

    public void save(Member member)
    {
        em.persist(member);
    }
}
