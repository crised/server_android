package com.telematic.spark.ws;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;

import com.telematic.spark.dao.MemberRepository;
import com.telematic.spark.model.Member;

/**
 * Example of JAX-WS class
 * 
 * @author lcestari
 * 
 */
@WebService(serviceName = "MemberWSService", portName = "MemberWSPort", name = "MemberWS", targetNamespace = "http://www.telematic.com/soap")
// suggestion
public class MemberWS {

	@Inject
	private MemberRepository repository;

	@WebMethod
	// not necessary
	public List<Member> listAllMembers(Date timestamp) {
		return repository.findCreatedSince(timestamp);
	}
}
