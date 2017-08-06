package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.simple.JSONObject;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import net.bitnine.domain.ConnectInfos;
import net.bitnine.domain.dto.DBConnectionInfo;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.jwt.DataSourceMap;
import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.jwt.UserInfoMap;
//import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
    @Autowired private DatabaseService databaseService;
    @Autowired private TokenAuthentication tokenAuthentication;
    @Autowired private JsonObjectRepository repository;

    @Autowired private UserInfoMap userInfoMap;
    
    public JSONObject getJson (String query, String Authorization) throws UnsupportedEncodingException, InvalidTokenException, QueryException, NamingException {
        
        Claims claims = tokenAuthentication.getClaims(Authorization);       // 해당토큰을 가져옴. getClaims()에서 유효성 검사.
        
        String userId = "";
        
        if (claims != null) {
        	userId = (String) claims.get("id");        
        }
        else {
            throw new InvalidTokenException();
        }
        
        DBConnectionInfo dbConnectionInfo = userInfoMap.getUserInfos().get(userId);
                
        DataSource dataSource = databaseService.createDataSource(dbConnectionInfo);
        
        System.out.println("dataSource: " + dataSource);

        repository.setDataSource(dataSource);
        
        JSONObject jsonList = repository.getJson(query);
        
        return jsonList;
    }
}





































