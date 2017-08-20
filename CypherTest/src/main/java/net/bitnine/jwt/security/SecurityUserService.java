package net.bitnine.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import net.bitnine.repository.MemberRepository;

@Service
@Log
public class SecurityUserService implements UserDetailsService {
    @Autowired MemberRepository repository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        /*repository.findOne(username)
            .filter(m -> m != null)
            .map(m -> new SecurityUser(m)).get();*/
        
        return null;
    }

}
