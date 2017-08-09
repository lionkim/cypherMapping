package net.bitnine.repository;

import org.springframework.data.repository.CrudRepository;

import net.bitnine.jwt.security.User;

public interface AuthenticationRepository extends CrudRepository<User, String> {

}
