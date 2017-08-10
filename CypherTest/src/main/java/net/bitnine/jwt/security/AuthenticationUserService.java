package net.bitnine.jwt.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;

@Service
@Log
public class AuthenticationUserService implements UserDetailsService {@Override
    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

}
