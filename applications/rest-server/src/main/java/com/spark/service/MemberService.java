import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.spark.model.Member;
import org.infinispan.Cache;

import com.spark.context.PendingCache;
import com.telematic.spark.model.Member;
import com.telematic.spark.ws.client.MemberWSService;

@Stateless
public class MemberService {
    @Inject
    Logger log;

    @Inject
    @PendingCache
    Cache<Long, Member> cache;

    public List<Member> findAllOrderedByName() {
        Collection<Member> col = cache.values();
        log.info("cache size @" + cache.size());
        return new ArrayList<Member>(col);
    }

    public Member findById(long id) {
        return cache.get(id);
    }

    public Member findByEmail(String email) {
        // TODO Auto-generated method stub
        return null;
    }

    public void save(Member member) {
        com.telematic.spark.ws.client.Member m = new com.telematic.spark.ws.client.Member();

        Date d = member.getCreatedOn();
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTime(member.getCreatedOn());

        try {
            m.setCreatedOn(DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(gcal));
        } catch (DatatypeConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        m.setEmail(member.getEmail());
        m.setName(member.getName());
        m.setId(member.getId());
        m.setPhoneNumber(member.getPhoneNumber());
        m.setVersion(member.getVersion());

        cache.remove(member.getId());
        new MemberWSService().getMemberWSPort().save(m);
    }

    public void append(List<com.telematic.spark.ws.client.Member> mems) {
        for (com.telematic.spark.ws.client.Member m : mems) {
            log.log(Level.INFO, "add memeber into cache ...@" + m.toString());
            Member member = new Member();
            member.setId(m.getId());
            member.setEmail(m.getEmail());
            member.setName(m.getName());
            member.setPhoneNumber(m.getPhoneNumber());
            member.setVersion(m.getVersion());
            member.setCreatedOn(new Date(m.getCreatedOn().getYear(), m
                    .getCreatedOn().getMonth(), m.getCreatedOn().getDay(), m
                    .getCreatedOn().getHour(), m.getCreatedOn().getMinute(), m
                    .getCreatedOn().getSecond()));
            cache.put(member.getId(), member);
        }
    }
}