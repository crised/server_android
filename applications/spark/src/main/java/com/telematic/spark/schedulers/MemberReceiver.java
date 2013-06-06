package com.telematic.spark.schedulers;

import com.telematic.spark.dao.MemberRepository;
import com.telematic.spark.ws.client.Member;
import com.telematic.spark.ws.client.MemberWSService;
import org.jboss.solder.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.GregorianCalendar;
import java.util.List;

@Singleton
@ApplicationScoped
public class MemberReceiver {

    private XMLGregorianCalendar lastMemberRegistrationDate;

    @SuppressWarnings("CdiInjectionPointsInspection")
    @Inject
    private Logger log;

    @Inject
    private MemberRepository memberRepository;

    private void persistNewMember(Member m)
    {
        com.telematic.spark.model.Member member = new com.telematic.spark.model.Member();
        member.setName(m.getName());
        member.setEmail(m.getEmail());
        member.setPhoneNumber(m.getPhoneNumber());
        member.setCreatedOn(m.getCreatedOn().toGregorianCalendar().getTime());
        memberRepository.save(member);
    }

    /**
     * This method is scheduled every 30 seconds and receive new Members from ACME server and persist them to db
     */
    @SuppressWarnings("UnusedDeclaration")
    @Schedule(hour = "*", minute = "*", second = "*/30", info = "MemberReceiver scheduled")
    private void retrieve()
    {


        if (lastMemberRegistrationDate == null) {
            log.info("Unknown last member registration date...");
            setLastMemberRegistrationDate();
        }

        List<Member> newMemberList = new MemberWSService().getMemberWSPort().listAllMembers(lastMemberRegistrationDate);

        for (Member m : newMemberList) {
            persistNewMember(m);

            if (m.getCreatedOn().compare(lastMemberRegistrationDate) > 0) {
                lastMemberRegistrationDate = m.getCreatedOn();
            }
        }
    }

    private void setLastMemberRegistrationDate()
    {
        try {
            com.telematic.spark.model.Member theNewest = memberRepository.findTheNewest();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(theNewest.getCreatedOn());

            try {
                lastMemberRegistrationDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            } catch (DatatypeConfigurationException e) {
                e.printStackTrace();
            }
        } catch (NoResultException e) {
            e.printStackTrace();
        }
    }
}
