package net.bitnine.jwt.security.test;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MemberRepository extends CrudRepository<Member, String> {

	Optional<Member> findByUid(String uid);

}
