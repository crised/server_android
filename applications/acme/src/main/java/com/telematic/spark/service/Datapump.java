package com.telematic.spark.service;

import com.telematic.spark.model.Member;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.logging.Logger;

@Stateless
public class Datapump {

    @Inject
    private EntityManager em;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private Logger log;

    @Schedule(hour = "*", minute = "*/1")
    public void insertDummyData()
    {
        log.info("insert dummy data...");
        Member newMember = new Member();
        newMember.setEmail(new Date().getTime() + "@test.com");
        newMember.setName("Name");
        newMember.setPhoneNumber("1234567890");
        newMember.setCreatedOn(new Date());

        em.persist(newMember);
    }
}
