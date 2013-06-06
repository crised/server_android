package com.telematic.spark.service;

import com.telematic.spark.model.Member;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;

/**
 * An example of service, the package is a symbolic reference to the business
 * layer
 *
 * @author lcestari
 *
 */
@Stateless
public class MemberRegistration {

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Member> memberEventSrc;

    public void register(Member member) throws Exception {
        log.info("Registering " + member.getName());
        em.persist(member);
        memberEventSrc.fire(member);
    }
}
