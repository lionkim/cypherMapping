package net.bitnine.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import net.bitnine.domain.dto.DataSourceDTO;

/*import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

import static java.util.Collections.emptyList;

import java.security.Key;
import java.util.Base64;

public class TokenAuthentication {
	/*static final long EXPIRATIONTIME = 864_000_000; // 10 days
	static final String SECRET = "ThisIsASecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";*/

    private static final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretByptes = Base64.getEncoder().encodeToString(secretBytes);
    
    public static String generateToken(DataSourceDTO dataSourceDTO) {
        String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30));

        System.out.println("base64SecretByptes: " + base64SecretByptes);
        System.out.println("id: " + id);
       
        String token = Jwts.builder()
                .setId(id)
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
//                .setIssuedAt(now)
//                .setNotBefore(now)
//                .setExpiration(exp)
                .claim("url", dataSourceDTO.getUrl())
                .claim("username", dataSourceDTO.getUsername())
                .claim("password", dataSourceDTO.getPassword())
                .signWith(SignatureAlgorithm.HS256, base64SecretByptes).compact();
        
        return token;        
    }
    
    public static Claims verifyToken(String token) {
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
          
        return claims;
    }
    
	/*static void addAuthentication(HttpServletResponse res, String username) {
		String JWT = Jwts.builder().setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			// parse the token.
			String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody()
					.getSubject();

			return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
		}
		return null;
	}*/
}