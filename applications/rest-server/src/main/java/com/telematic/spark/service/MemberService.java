package com.telematic.spark.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.infinispan.Cache;

import com.telematic.spark.context.PendingCache;
import com.telematic.spark.ws.client.Member;
import com.telematic.spark.ws.client.MemberWSService;

@Stateless
public class MemberService {
	
	@Inject
	@PendingCache
	Cache<Long, Member> cache;

	public List<Member> findAllOrderedByName() {
		return (List<Member>) cache.values();
	}

	public Member findById(long id) {
		return cache.get(id);
	}

	public Member findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Member member) {
		cache.remove(member.getId());
		new MemberWSService().getMemberWSPort()
		.save(member);
	}

}
