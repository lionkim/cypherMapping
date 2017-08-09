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
public class AuthenticationRepostoryTest {

	@Autowired private AuthenticationRepository repository;

	private static final String userId01 = "agraph";
	private static final String userId02 = "test01";
	private static final String userPW01 = "agraph";
	private static final String userPW02 = "test01";
	
	@Test
	public void testRead() {
		User user01 = repository.findOne(userId01);
		User user02 = repository.findOne(userId02);

		assertThat(user01.getPassword(), is(userPW01));
		assertThat(user02.getPassword(), is(userPW02));
		
		assertThat(user01.getUserRole().getRoleId(), is(21));
		assertThat(user02.getUserRole().getRoleId(), is(22));

		assertThat(user01.getUserRole().getRole(), is(Role.ADMIN));
		assertThat(user02.getUserRole().getRole(), is(Role.USER));
		
		/*assertThat(user01.getUserRole().get(0), is(21));
		assertThat(user02.getUserRole().get(1), is(22));

		assertThat(user01.getUserRole().get(0).getRole(), is(Role.ADMIN));
		assertThat(user02.getUserRole().get(1).getRole(), is(Role.USER));*/
	}
}



















