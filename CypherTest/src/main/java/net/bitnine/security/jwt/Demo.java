package net.bitnine.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class Demo {
    
    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretByptes = Base64.getEncoder().encodeToString(secretBytes);
    


    public static void main(String[] args) {
        System.out.println(generateToken());
        verifyToken(generateToken());
//        checkToken(createToken());
    }


    
    private static String generateToken() {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30));
        
        String token = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .claim("url", "jdbc:postgresql://27.117.163.21:15602/imdb")
                .claim("username", "agraph")
                .claim("password", "agraph")
                .signWith(SignatureAlgorithm.HS256, base64SecretByptes).compact();
        
        return token;        
    }
    
    private static void verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretByptes)
                .parseClaimsJws(token).getBody();
        

        System.out.println("----------------------------");
        System.out.println("ID: " + claims.getId());
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Issuer: " + claims.getIssuer());
        System.out.println("Expiration: " + claims.getExpiration());
        System.out.println("url: " + (String) claims.get("url"));
        System.out.println("username: " + (String) claims.get("username"));
        System.out.println("password: " + (String) claims.get("password"));
                

            
    }


    
    private static String createToken() {
        String jwt = "";
        try {
            jwt = Jwts.builder()
                    .setSubject("users/TzMUocMF4p")
                    .setExpiration(new Date(1300819380))
                    .claim("name", "Robert Token Man")
                    .claim("scope", "self groups/admins")
                    .signWith(SignatureAlgorithm.HS256, "secret".getBytes("UTF-8"))
                    .compact();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jwt;
    }
    
    private static void checkToken(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey("secret".getBytes("UTF-8"))
                    .parseClaimsJws(jwt);

            String scope = (String) claims.getBody().get("scope");
            assertEquals(scope, "self groups/admins");
            
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }                
    }
}





































