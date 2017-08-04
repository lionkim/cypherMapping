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
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.exception.QueryException;
import net.bitnine.jwt.DataSourceMap;
//import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
   /* @Autowired private DatabaseService databaseService;
    @Autowired private TokenAuthentication tokenAuthentication;*/
    @Autowired private JsonObjectRepository repository;

    @Autowired private DataSourceMap dataSourceMap;
    
    public JSONObject getJson (String query, String Authorization) throws UnsupportedEncodingException, InvalidTokenException, QueryException, NamingException {
        
        
//        DataSource dataSource = dataSourceMap.getDataSources().get(Authorization);
//        PGPoolingDataSource pgPoolingDataSource = dataSourceMap.getDataSources().get(Authorization);

        Authorization = Authorization.replaceAll("\\.","");

//        BasicDataSource dataSource = (BasicDataSource) new InitialContext().lookup("java:comp/env/jdbc/" + Authorization);
//        BasicDataSource dataSource = (BasicDataSource) new InitialContext().lookup(Authorization);
        
        PGPoolingDataSource dataSource = (PGPoolingDataSource) new InitialContext().lookup("java:/comp/env/jdbc/" + Authorization);
//        PGPoolingDataSource pgPoolingDataSource = (PGPoolingDataSource) new InitialContext().lookup(Authorization);
        
//        TokenAuthentication tokenAuthentication = new TokenAuthentication();
        /*Claims claims = tokenAuthentication.getClaims(Authorization);       // 해당토큰을 가져옴. getClaims()에서 유효성 검사.
        
        DataSourceDTO dataSourceDTO = new DataSourceDTO();

        if (claims != null) {
            dataSourceDTO.setUrl((String) claims.get("url"));
            dataSourceDTO.setUsername((String) claims.get("username"));
            dataSourceDTO.setPassword((String) claims.get("password"));
        }
        else {
            throw new InvalidTokenException();
        }*/
        
        //DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
        
        System.out.println("dataSource: " + dataSource);

        repository.setDataSource(dataSource);
//        repository.setDataSource(pgPoolingDataSource);
        
        JSONObject jsonList = repository.getJson(query);
        
        return jsonList;
    }
}





































