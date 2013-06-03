package com.telematic.spark.ws;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;

import com.telematic.spark.dao.MemberRepository;
import com.telematic.spark.model.Member;

/**
 * Example of JAX-WS class
 * 
 * @author lcestari
 *
 */
@WebService //(serviceName = "MemberWS", portName = "MemberWS", name = "MemberWS", targetNamespace = "http://www.telematic.com/spark/Member") //suggestion
public class MemberWS {
	
    @Inject
    private MemberRepository repository;

	//@WebMethod //not necessary 
	public List<Member> listAllMembers() {
		return repository.findAllOrderedByName();
	}
}
