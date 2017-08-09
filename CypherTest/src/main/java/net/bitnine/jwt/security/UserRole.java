package net.bitnine.jwt.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "USER_ROLE")
public class UserRole {

    @Id
	private int roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    protected Role role;
}
