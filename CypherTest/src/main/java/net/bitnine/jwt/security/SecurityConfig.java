package net.bitnine.jwt.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.java.Log;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@Log
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired SecurityUserService authenticationUserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("security config......................");
		
        http.authorizeRequests().antMatchers("/h2console").permitAll();
		
		http.authorizeRequests().antMatchers("/api/v1/db/login").permitAll();

        http.authorizeRequests().antMatchers("/api/v1/db/query").hasRole("USER");
        
        http.authorizeRequests().antMatchers("/api/v1/db/admin").hasRole("ADMIN");
		
		http.formLogin().loginPage("/api/v1/db/testLogin");
		
		http.exceptionHandling().accessDeniedPage("/accessDenied");
		http.logout().logoutUrl("/logout").invalidateHttpSession(true);
		
		http.userDetailsService(authenticationUserService);
	}
	
	
}




































