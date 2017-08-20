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
import net.bitnine.jwt.security.Member;
import net.bitnine.jwt.security.MemberRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@Log
public class AuthenticationRepostoryTest {

	@Autowired private MemberRepository repository;

	private static final String userId01 = "agraph";
	private static final String userId02 = "test01";
	private static final String userPW01 = "agraph";
	private static final String userPW02 = "test01";
	
	@Test
	public void testRead() {
		Member user01 = repository.findOne(userId01);
		Member user02 = repository.findOne(userId02);

		assertThat(user01.getPassword(), is(userPW01));
		assertThat(user02.getPassword(), is(userPW02));
		
		assertThat(user01.getRoles().getRoleId(), is(21));
		assertThat(user02.getRoles().getRoleId(), is(22));

		assertThat(user01.getRoles().getRole(), is(Role.ADMIN));
		assertThat(user02.getRoles().getRole(), is(Role.USER));
		
		/*assertThat(user01.getRoles().get(0), is(21));
		assertThat(user02.getRoles().get(1), is(22));

		assertThat(user01.getRoles().get(0).getRole(), is(Role.ADMIN));
		assertThat(user02.getRoles().get(1).getRole(), is(Role.USER));*/
	}
}



















