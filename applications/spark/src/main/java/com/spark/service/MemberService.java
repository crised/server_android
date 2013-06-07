package com.spark.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.Cache;

import com.deludo.ws.client.Member;
import com.deludo.ws.client.MemberWSService;
import com.spark.context.PendingCache;

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
		com.deludo.ws.client.Member m = new com.deludo.ws.client.Member();

		Date d = member.getCreatedOn();
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTime(member.getCreatedOn());

		cache.remove(member.getId());
		new MemberWSService().getMemberWSPort().save(member);
	}

	public void append(List<com.deludo.ws.client.Member> mems) {
		for (com.deludo.ws.client.Member member : mems) {
			log.log(Level.INFO,
					"add memeber into cache ...@" + member.toString());
			cache.put(member.getId(), member);
		}
	}
}