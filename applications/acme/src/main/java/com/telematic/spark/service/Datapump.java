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
    EntityManager em;

    @Inject
    Logger log;

    @Schedule(minute = "*/1")
    public void insertDummyData()
    {

        log.info("insert dummy data...");
        Member newMember = new Member();
        newMember.setEmail("test@test.com");
        newMember.setName("Name" + new Date());
        newMember.setPhoneNumber("1234");
        newMember.setCreatedOn(new Date());

        em.persist(newMember);
    }
}
