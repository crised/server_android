package com.deludo.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.deludo.model.Member;
import com.deludo.model.Member_;

/**
 * The example of a repository for the Member entity (the package is a symbolic
 * reference to the persistence layer.
 * 
 * @author lcestari
 * 
 */

@Stateless
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    public Member findByEmail(String email) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).where(cb.equal(member.get(Member_.name), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Member> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
        Root<Member> member = criteria.from(Member.class);
        criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        return em.createQuery(criteria).getResultList();
    }

	public List<Member> findCreatedSince(Date timestamp) {

		return em.createQuery("from Member where createdOn>:created")
		.setParameter("created", timestamp)
		.getResultList();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void merge(Member mem) {
		em.merge(mem);
	}
}
