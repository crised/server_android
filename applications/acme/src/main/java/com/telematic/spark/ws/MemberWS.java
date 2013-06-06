package com.telematic.spark.ws;

import com.telematic.spark.dao.MemberRepository;
import com.telematic.spark.model.Member;

import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

/**
 * Example of JAX-WS class
 *
 * @author lcestari
 */
@WebService(serviceName = "MemberWSService", portName = "MemberWSPort", name = "MemberWS", targetNamespace = "http://www.telematic.com/soap")
// suggestion
public class MemberWS {

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    Logger log;

    @Inject
    private MemberRepository repository;

    @WebMethod
    // not necessary
    public List<Member> listAllMembers(Date timestamp)
    {
        log.info("ACME WS invoked..." + timestamp);
        if (timestamp == null) {
            return repository.findAll();
        }
        return repository.findCreatedSince(timestamp);
    }
}
