package net.bitnine.controller;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.postgresql.ds.PGPoolingDataSource;
import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import net.bitnine.domain.ConnectInfo;
import net.bitnine.domain.ConnectInfos;
import net.bitnine.domain.State;
import net.bitnine.domain.dto.DBConnectionInfo;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.jwt.DataSourceMap;
import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.jwt.UserInfoMap;
import net.bitnine.service.DatabaseService;
import net.bitnine.util.JDBCTutorialUtilities;

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

    @Autowired private ConnectInfos connectInfos;
    @Autowired private TokenAuthentication tokenAuthentication;
    
    @Autowired private DataSourceMap dataSourceMap;

	@Autowired private UserInfoMap userInfoMap;    
	
    @RequestMapping("/connect")
    public JSONObject connect(DBConnectionInfo dbConnectionInfo) throws NamingException, SQLException {

    	JSONObject jsonObject = new JSONObject();     
    	
    	checkValidDataSource(dbConnectionInfo);		// 사용자로부터 전달받은 dbconnect 정보로 생성한 dataSource의 유효성을 체크 

    	String id = generateId();		// 사용자 정보 아이디, token 아이디 생성

        String tokenString = tokenAuthentication.generateToken(id, dbConnectionInfo);		// token 아이디와 사용자로부터 전달받은 dbconnect 정보로 token 생성.
        
        saveConnectionInfo(id, dbConnectionInfo);	// 사용자 db 접속정보를 application scope 객체 에 저장.
        
        jsonObject.put("token", tokenString);
        
        jsonObject.put("message", "Database Connect Success");
        
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
		userInfoMap.getUserInfos().put(id, dbConnectionInfo);		
	}
    
    @RequestMapping("/disconnect")
    public String disConnect(@RequestHeader(value="Authorization") String authorization)  {
        
        /*ConnectInfo connectInfo = connectInfos.getConnectInfoList().stream()                        // Convert to steam
                    .filter(x -> Authorization.equals(x.getToken()))        // we want Authorization only
                    .findAny()                                      // If 'findAny' then return found
                    .orElse(null);  */ 
        ConnectInfo connectInfo = null;
        for (ConnectInfo connInfo : connectInfos.getConnectInfoList()) {
            if ( authorization.equals(connInfo.getToken()) ) {
                connectInfo = connInfo;
            }
        }
        
        if (connectInfo == null) {
            return "Database DisConnect Failed";            
        }
        else {
            connectInfo.setState(State.INVALID);    // 해당토큰 정보의 상태를 INVALID로 설정.
        }

        return "Database DisConnect Success";
    }
	
	/*
	
	@RequestMapping("/connect")
	public String connect(DataSourceDTO dataSourceDTO, HttpSession session, Model model)  {

		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
//		System.out.println("dataSource: " + dataSource);
		
		if (dataSource != null) {
			session.setAttribute(DATASOURCE, dataSource);
			return "Database Connect Success";
		}
		return "Database Connect Failed";
	}*/
}



























