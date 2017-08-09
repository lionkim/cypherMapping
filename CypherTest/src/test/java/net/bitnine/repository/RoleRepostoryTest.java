package net.bitnine.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.configuration.plugins.Plugins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.java.Log;
import net.bitnine.jwt.security.Role;
import net.bitnine.jwt.security.User;
import net.bitnine.jwt.security.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class RoleRepostoryTest {

	@Autowired private RoleRepository repository;

	private static final int userRoleId01 = 21;
	private static final int userRoleId02 = 22;

	@Test
	public void testRead() {
		UserRole userRole01 = repository.findOne(userRoleId01);
		UserRole userRole02 = repository.findOne(userRoleId02);
		
		assertThat(userRole01.getRoleId(), is(userRoleId01));
		assertThat(userRole02.getRoleId(), is(userRoleId02));

		assertThat(userRole01.getRole(), is(Role.ADMIN));
		assertThat(userRole02.getRole(), is(Role.USER));
	}
}



















