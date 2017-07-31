package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.exception.InvalidTokenException;
import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
    @Autowired private DatabaseService databaseService;
	JsonObjectRepository repository;

    public JSONObject getJson (String query, String Authorization) throws UnsupportedEncodingException, InvalidTokenException {
        TokenAuthentication tokenAuthentication = new TokenAuthentication();
        Claims claims = tokenAuthentication.getClaims(Authorization);       // 해당토큰을 가져옴. getClaims()에서 유효성 검사.
        
        DataSourceDTO dataSourceDTO = new DataSourceDTO();

        if (claims != null) {
            dataSourceDTO.setUrl((String) claims.get("url"));
            dataSourceDTO.setUsername((String) claims.get("username"));
            dataSourceDTO.setPassword((String) claims.get("password"));
        }
        else {
            throw new InvalidTokenException();
        }
        
        DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
        
        System.out.println("dataSource: " + dataSource);
        
        repository = new JsonObjectRepository(dataSource);
        
        JSONObject jsonList = repository.getJson(query);
        
        return jsonList;
    }
}





































