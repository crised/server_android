package com.telematic.spark.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.infinispan.Cache;

import com.telematic.spark.context.PendingCache;
import com.telematic.spark.ws.client.Member;
import com.telematic.spark.ws.client.MemberWSService;

@Stateless
public class DataRetriever {

	@Inject
	@PendingCache
	Cache<Long, Member> cache;

	@Inject
	Logger log;

	/**
	 * Default constructor.
	 */
	public DataRetriever() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Schedule(minute = "*/30")
	private void retrieve(final Timer t) {
		System.out.println("@Schedule called at: " + new java.util.Date());

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MINUTE, -30);

		XMLGregorianCalendar cal = null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
					calendar);

		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Member> mems = new MemberWSService().getMemberWSPort()
				.listAllMembers(cal);

		for (Member m : mems) {
			log.log(Level.INFO, "add memeber into cache ...@" + m.toString());
			cache.put(m.getId(), m);
		}
	}
}