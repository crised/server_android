package com.telematic.spark.dao;

import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.telematic.spark.ws.client.Member;
import com.telematic.spark.ws.client.MemberWSService;

public class MemberRepository {

	public List<Member> findAllOrderedByName() {
		XMLGregorianCalendar cal=null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MemberWSService().getMemberWSPort().listAllMembers(cal);
	}

	public Member findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Member findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Member member) {
		// TODO Auto-generated method stub
		
	}

}
