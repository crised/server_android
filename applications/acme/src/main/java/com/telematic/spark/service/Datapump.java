package com.telematic.spark.service;

import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.telematic.spark.model.Member;

@Stateless
public class Datapump {

	@Inject
	Logger log;

	@Inject
	EntityManager em;

	@Schedule(minute = "*/5")
	public void insertDummyData(final Timer t) {

		log.info("insert dummy data...");
		Member newMember = new Member();
		newMember.setEmail("test@test.com");
		newMember.setName("Name"+new Date());
		newMember.setPhoneNumber("1234");
		newMember.setCreatedOn(new Date());
		
		em.persist(newMember);
	}
}
