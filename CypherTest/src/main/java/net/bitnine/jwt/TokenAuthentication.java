package net.bitnine.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import net.bitnine.domain.ConnectInfo;
import net.bitnine.domain.ConnectInfos;
import net.bitnine.domain.State;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.exception.InValidDataSourceException;
import net.bitnine.service.DatabaseService;
import net.bitnine.service.PropertiesService;

/*import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static java.util.Collections.emptyList;

import java.security.Key;
import java.util.Base64;

@Component
public class TokenAuthentication {
    
	@Autowired private DatabaseService databaseService;
	
    @Autowired private ConnectInfos connectInfos;
        
    private final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);
    private final byte[] secretBytes = secret.getEncoded();
    private final String base64SecretByptes = Base64.getEncoder().encodeToString(secretBytes);    
    
    
    public String generateToken(DataSourceDTO dataSourceDTO) {   
    	
    	//if (databaseService.createDataSource(dataSourceDTO) == null) throw new InValidDataSourceException();
    	
    	String id = UUID.randomUUID().toString().replace("-", "");
        Date now = new Date();
//        int temp = (1000 * Integer.parseInt(setMax));
        int temp = (1000 * 60 * 60);

        System.out.println("temp: " + temp);
        
        Date exp = new Date(System.currentTimeMillis() + temp);

       /* System.out.println("base64SecretByptes: " + base64SecretByptes);
        System.out.println("id: " + id);*/
        
        // JJWT 를 사용하여 token 생성
        String token = Jwts.builder()
                .setId(id)
                .setHeaderParam("alg", "HS256")				// token algorithm
                .setHeaderParam("typ", "JWT")				// token type
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)								// expired time
               /* .claim("url", dataSourceDTO.getUrl())
                .claim("username", dataSourceDTO.getUsername())
                .claim("password", dataSourceDTO.getPassword())*/
                .signWith(SignatureAlgorithm.HS256, base64SecretByptes).compact();
        
        return token;        
    }
    
    /**
     * Authorization에 해당하는 Claims을 리턴.
     * @param Authorization
     * @return
     */
    public Claims getClaims (String Authorization) {

        Claims claims = null;
        ConnectInfo connectInfo;
        
        try {
            connectInfo = verifyToken(Authorization); 
            
            if (connectInfo == null) {
                return claims;          
            }
            else {
                claims = Jwts.parser()
                        .setSigningKey(base64SecretByptes)
                        .parseClaimsJws(Authorization).getBody();
            }
        }
        catch (Exception e) {
            return claims;          
        }
        return claims;
    }

    /**
     * connectInfos의 connectInfoList에서 Authorization에 해당하는 ConnectInfo를 찾아서 상태를 확인하여 
     * 상태가 VALID일 경우 해당하는 ConnectInfo를 반환.
     * 상태가 INVALID 이거나  해당하는 ConnectInfo가 없으면 null을 반환
     */
    private ConnectInfo verifyToken(String Authorization) {
        ConnectInfo connectInfo;
        connectInfo = connectInfos.getConnectInfoList().stream()                        // Convert to steam
                .filter(x -> Authorization.equals(x.getToken()))        // we want Authorization only
                .findAny()                                      // If 'findAny' then return found
                .orElse(null);
        
        if ( (connectInfo == null) || ((connectInfo != null) && (connectInfo.getState() == State.INVALID)) ) {
            return null;
        }
        return connectInfo;
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