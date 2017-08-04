package net.bitnine.controller;

import java.sql.SQLException;
import java.util.Date;

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
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.jwt.DataSourceMap;
import net.bitnine.jwt.TokenAuthentication;
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
    
    @RequestMapping("/connect")
    public JSONObject connect(DataSourceDTO dataSourceDTO) throws QueryException, NamingException {
        JSONObject jsonObject = new JSONObject();
//        DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
        
        
//      System.out.println("dataSource: " + dataSource);
        
        String tokenString = tokenAuthentication.generateToken(dataSourceDTO);

        databaseService.createPGPoolingDataSource(dataSourceDTO, tokenString);
        
//            dataSourceMap.getDataSources().put(tokenString, pgPoolingDataSource);            
//            System.out.println("pgPoolingDataSource: " + pgPoolingDataSource);
        
         /* dataSourceMap.getDataSources().put(tokenString, dataSource);            
        System.out.println("dataSource: " + dataSource);*/
        
        jsonObject.put("token", tokenString);
        
        jsonObject.put("message", "Database Connect Success");
        
        return jsonObject;
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



























