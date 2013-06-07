package com.spark.service;

import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.Timer;

import com.deludo.ws.client.Member;
import com.deludo.ws.client.MemberWSService;



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

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -5);

		List<Member> mems = new MemberWSService().getMemberWSPort()
				.listAllMembers(calendar.getTime());

		svc.append(mems);
	}
}