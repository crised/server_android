package com.telematic.spark.service;

import java.util.Date;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.telematic.spark.model.Member;

@Stateless
public class Datapump {

	@PersistenceContext
	EntityManager em;
	
	public Datapump(){}

	@Schedule(minute="*/5", hour="*", persistent=false)
	private void insert(final Timer t) {

		System.out.println("insert dummy data...");
		Member newMember = new Member();
		newMember.setEmail("test@test.com");
		newMember.setName("Name"+new Date());
		newMember.setPhoneNumber("1234567890");
		newMember.setCreatedOn(new Date());
		
		em.persist(newMember);
	}
}
