package net.bitnine.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;

public class JavaJsonWebToken {
	Key key = MacProvider.generateKey();

	String compactJws = Jwts.builder()
			.setSubject("Joe")
			.signWith(SignatureAlgorithm.HS512, key)
			.compact();
	
	public static void main(String[] args) {

		Key key = MacProvider.generateKey();
		
        String privateKey = "privateKey";
        
        String publicKey = "publicKey";

         String compactedJWT =  Jwts.builder().setSubject("syle")
                 .setIssuer("localhost")
     			.signWith(SignatureAlgorithm.HS512, key)
//                 .signWith(SignatureAlgorithm.RS256, key)
                 .compact();
         System.out.println(compactedJWT);
    }
}
