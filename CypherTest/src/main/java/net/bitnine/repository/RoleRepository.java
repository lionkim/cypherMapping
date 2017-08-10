package net.bitnine.repository;

import org.springframework.data.repository.CrudRepository;

import net.bitnine.jwt.security.MemberRole;

public interface RoleRepository extends CrudRepository<MemberRole, Integer>{

}
