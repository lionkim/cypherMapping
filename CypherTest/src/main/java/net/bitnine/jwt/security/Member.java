package net.bitnine.jwt.security;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {

    @Id
	private String id;

    @Column
	private String password;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private MemberRole roles;
    
    /*@ManyToOne
    @JoinColumn(name = "role_id")
	private MemberRole UserRole;*/
    
    /*@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private List<UserRole> UserRole;*/
	
}
