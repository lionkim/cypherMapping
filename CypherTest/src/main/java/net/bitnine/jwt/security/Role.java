package net.bitnine.jwt.security;

public enum Role {
    ADMIN, USER;
    
    public String authority() {
        return "ROLE_" + this.name();
    }

}
