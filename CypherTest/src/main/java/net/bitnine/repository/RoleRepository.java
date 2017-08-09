package net.bitnine.repository;

import org.springframework.data.repository.CrudRepository;

import net.bitnine.jwt.security.UserRole;

public interface RoleRepository extends CrudRepository<UserRole, Integer>{

}
