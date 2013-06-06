package com.telematic.spark.service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.telematic.spark.ws.client.Member;
import com.telematic.spark.ws.client.MemberWSService;

@Stateless
public class DataRetriever {

	@EJB
	MemberService svc;	

	/**
	 * Default constructor.
	 */
	public DataRetriever() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unused")
	@Schedule(minute="*/5", hour="*", persistent=false)
	private void retrieve(final Timer t) {
		System.out.println("@Schedule called at: " + new java.util.Date());

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.MINUTE, -5);

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

		svc.append(mems);
	}
}