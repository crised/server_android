package com.deludo.ws;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.deludo.dao.MemberRepository;
import com.deludo.model.Member;

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
	public List<Member> listAllMembers(
			@WebParam(name = "dateAfter") Date timestamp) {
		return repository.findCreatedSince(timestamp);
	}

	@WebMethod
	public void save(@WebParam(name = "member") Member mem) {
		repository.merge(mem);
	}

    @WebMethod
    public List<Member> listAll(){
        return repository.findAllOrderedByName();
    }

}
