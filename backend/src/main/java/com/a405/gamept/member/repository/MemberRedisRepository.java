package com.a405.gamept.member.repository;

import com.a405.gamept.member.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRedisRepository extends CrudRepository<Member, String> {
}
