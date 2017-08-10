package net.bitnine.repository;

import org.springframework.data.repository.CrudRepository;

import net.bitnine.jwt.security.Member;

public interface MemberRepository extends CrudRepository<Member, String> {

}
