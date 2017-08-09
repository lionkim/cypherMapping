package net.bitnine.jwt.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

    @Id
	private String id;

    @Column
	private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
	private UserRole UserRole;
    
    /*@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private List<UserRole> UserRole;*/
	
}
