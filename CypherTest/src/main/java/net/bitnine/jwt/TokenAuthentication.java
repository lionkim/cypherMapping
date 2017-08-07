package net.bitnine.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import net.bitnine.jwt.ConnectInfo;
import net.bitnine.domain.dto.DBConnectionInfo;
import net.bitnine.exception.InValidDataSourceException;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.service.DatabaseService;
import net.bitnine.service.PropertiesService;
import net.bitnine.util.JDBCTutorialUtilities;

/*import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;*/

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

import static java.util.Collections.emptyList;

import java.security.Key;
import java.sql.SQLException;
import java.util.Base64;

/**
 * 사용자 토큰 생성
 * @author cppco
 *
 */
@Component
public class TokenAuthentication {
    
	@Autowired private DatabaseService databaseService;
	
	@Autowired private UserInfoMap userInfoMap;    
        
    private final Key secret = MacProvider.generateKey(SignatureAlgorithm.HS256);			// 비밀키생성
    private final byte[] secretBytes = secret.getEncoded();
    private final String base64SecretByptes = Base64.getEncoder().encodeToString(secretBytes);    	// 비밀키로 base64 인코딩
    
    /**
     * 토큰생성, 사용 dbConnection정보확인 및 저장
     * @param id 
     * @param dbConnectionInfo
     * @return
     * @throws SQLException 
     */
    public String generateToken(String id, DBConnectionInfo dbConnectionInfo) throws SQLException {       	    	

		Date now = new Date();
        int temp = (1000 * 60 * 60 * 24);		// 1000 millisecond.  1초 * 60 * 60 * 24 = 1일
        
        Date exp = new Date(System.currentTimeMillis() + temp);	// 만료시간
        
        // JJWT 를 사용하여 token 생성
        String token = Jwts.builder()
                .setId(id)
                .setHeaderParam("alg", "HS256")				// token algorithm
                .setHeaderParam("typ", "JWT")				// token type
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)								// expired time
                .claim("id", id)									// 구별 id를 저장
                .signWith(SignatureAlgorithm.HS256, base64SecretByptes).compact();
		return token;
    }

    /**
     * token에 해당하는 Claims을 리턴.
     * @param token
     * @return
     */
    public Claims getClaimsInToken (String token) {

        Claims claims = null;
        
        try {
            claims = Jwts.parser()
                    .setSigningKey(base64SecretByptes)
                    .parseClaimsJws(token).getBody();
        }
        catch (Exception e) {
            return claims;          
        }
        return claims;
    }

    // 해당 토큰안에 있는 id를 가져오는 메소드
    public String getIdInToken(String token) {
        Claims claims = getClaimsInToken(token);       // 해당토큰을 가져옴.
        
        String userId = "";
        
        if (claims != null) {
            userId = (String) claims.get("id");        
        }
        else {
            throw new InvalidTokenException();
        }
        
        if (verifyToken(userId) == null) {
            throw new InvalidTokenException();
        }
        
        return userId;
    }
    /**
     * UserInfoMap의 UserInfos에서 key에 해당하는 ConnectInfo를 찾아서 상태를 확인하여 
     * 상태가 VALID일 경우 해당하는 ConnectInfo를 반환.
     * 상태가 INVALID 이거나  해당하는 ConnectInfo가 없으면 null을 반환
     */
    private String verifyToken(String key) {
        ConnectInfo connectInfo;
        
        connectInfo = userInfoMap.getUserInfos().get(key);
        
        if ( (connectInfo == null) || ((connectInfo != null) && (connectInfo.getState() == State.INVALID)) ) {
            return null;
        }
        return "success";
    }
    

    /**
     * token에 해당하는 Claims을 리턴.
     * @param token
     * @return
     */
    /*public Claims getClaims (String token) {

        Claims claims = null;
        ConnectInfo connectInfo;
        
        try {
            connectInfo = verifyToken(token); 
            
            if (connectInfo == null) {
                return claims;          
            }
            else {
                claims = Jwts.parser()
                        .setSigningKey(base64SecretByptes)
                        .parseClaimsJws(token).getBody();
            }
        }
        catch (Exception e) {
            return claims;          
        }
        return claims;
    }*/

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