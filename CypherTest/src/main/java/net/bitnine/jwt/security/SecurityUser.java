package net.bitnine.jwt.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityUser extends User {
    
    private static final String ROLE_PREFIX = "ROLE_";
    
    private Member member;
    
    public SecurityUser(Member member) {
        
        super(member.getId(), member.getPassword(), makeGrantedAuthority(member.getRoles()));
    }
    
    private static Collection<? extends GrantedAuthority> makeGrantedAuthority(List<MemberRole> roles) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        
        roles.forEach(
                role -> grantedAuthorityList.add(
                        new SimpleGrantedAuthority(ROLE_PREFIX + role.getRole())) );
        
        return grantedAuthorityList;
    }

}






























