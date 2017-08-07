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
        
        String userId = tokenAuthentication.getIdInToken(Authorization);            // 해당 토큰안에 있는 id를 가져오는 메소드
        
//        DBConnectionInfo dbConnectionInfo = userInfoMap.getUserInfos().get(userId);
        

        DBConnectionInfo dbConnectionInfo = userInfoMap.getUserInfos().get(userId).getDbConnectionInfo();
                
        DataSource dataSource = databaseService.createDataSource(dbConnectionInfo);
        
        System.out.println("dataSource: " + dataSource);

        repository.setDataSource(dataSource);
        
        JSONObject jsonList = repository.getJson(query);
        
        return jsonList;
    }
}





































