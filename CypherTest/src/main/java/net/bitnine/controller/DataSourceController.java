package net.bitnine.controller;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.naming.NamingException;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.dto.DBConnectionInfo;
import net.bitnine.jwt.ConnectInfo;
import net.bitnine.jwt.State;
import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.jwt.ConnectionInfoMap;
import net.bitnine.service.DatabaseService;

/**
 * 사용자가 전송한 정보로 dataSource를 만드는 컨트롤러
 * 세션에 dataSource를 저장, 해제
 * @author cppco
 *
 */
@RequestMapping("/api/v1/db/")
@RestController
public class DataSourceController {
	
	public static final String DATASOURCE = "dataSource";
	
	@Autowired private DatabaseService databaseService;

    @Autowired private TokenAuthentication tokenAuthentication;

	@Autowired private ConnectionInfoMap connectionInfoMap;    
	
	/**
	 *  사용자로부터 전달받은 dbconnect 정보를 가지고 token을 생성
	 *  dbconnect 정보 Validation
	 *  
	 * @param dbConnectionInfo
	 * @return
	 * @throws NamingException
	 * @throws SQLException
	 */
    @RequestMapping("/connect")
    public JSONObject connect(DBConnectionInfo dbConnectionInfo) throws NamingException, SQLException {
        
    	JSONObject jsonObject = new JSONObject();     
    	
    	checkValidDataSource(dbConnectionInfo);		// 사용자로부터 전달받은 dbconnect 정보로 생성한 dataSource의 유효성을 체크 

        String userId = generateId();            // id 생성
        String tokenString = tokenAuthentication.generateToken(userId, dbConnectionInfo);		// token 아이디와 사용자로부터 전달받은 dbconnect 정보로 token 생성.
        
        jsonObject.put("token", tokenString);
        
        jsonObject.put("message", "Database Connect Success");
        

        saveConnectionInfo(userId, dbConnectionInfo);   // 사용자 db 접속정보를 application scope 객체 에 저장.
        
        return jsonObject;
    }


	// token 아이디 생성
	private String generateId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
    
    // 사용자로부터 전달받은 정보로 생성한 dataSource의 유효성을 체크
    private void checkValidDataSource(DBConnectionInfo dbConnectionInfo) throws SQLException {    	
    	databaseService.checkValidDataSource(dbConnectionInfo);	
	}


    // 사용자 db 접속정보를 application scope 객체 에 저장.
	private void saveConnectionInfo(String id, DBConnectionInfo dbConnectionInfo) {
        
        ConnectInfo connectInfo = new ConnectInfo();       // 새로운 ConnectInfo 객체를 생성.        
        
//        connectInfo.setToken (id);           
        connectInfo.setConnetTime (stringCurrentTime());       // 현재 시간을 저장. 
        connectInfo.setQueryTimes(0);
        connectInfo.setState(State.VALID);
        connectInfo.setDbConnectionInfo(dbConnectionInfo);
        
        connectionInfoMap.getConnectInfos().put(id, connectInfo);      // connectInfos의 connectInfoList에 ConnectInfo 객체를 저장.        
	}
	
	// 현재 시간을 String형으로 반환.
    private String stringCurrentTime() {
        String timeFormat = "yyyy-MM-dd HH:mm:ss";        
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(timeFormat));
    }
    
	// Connect 해제
    @RequestMapping("/disconnect")
    public String disConnect(@RequestHeader(value="Authorization") String token)  {
        String userId = tokenAuthentication.getClaimsByToken(token).getId();            // 해당 토큰안에 있는 id를 가져오는 메소드
        
        ConnectInfo connectInfo = connectionInfoMap.getConnectInfos().get(userId);
                
        if (connectInfo == null) {
            return "Database DisConnect Failed";            
        }
        else {
            connectInfo.setState(State.INVALID);    // 해당토큰 정보의 상태를 INVALID로 설정.
        }

        return "Database DisConnect Success";
    }
}



























