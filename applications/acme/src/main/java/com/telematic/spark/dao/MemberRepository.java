package com.telematic.spark.dao;

import com.telematic.spark.model.Member;
import com.telematic.spark.model.Member_;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
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

    @SuppressWarnings("unchecked")
    public List<Member> findAll()
    {
        return em.createQuery("select m from Member m").getResultList();
    }

    public Member findByEmail(String email)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        return em.createQuery(criteria).getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Member> findCreatedSince(Date timestamp)
    {
        return em.createQuery("select m from Member m where m.createdOn>:created").setParameter("created", timestamp).getResultList();
    }
}
