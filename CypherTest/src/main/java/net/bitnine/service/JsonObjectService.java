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
import net.bitnine.jwt.TokenAuthentication;
import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
    @Autowired private DatabaseService databaseService;
	JsonObjectRepository repository;

    public JSONObject getJson (String query, String Authorization) throws UnsupportedEncodingException {
        Claims claims = TokenAuthentication.verifyToken(Authorization);
        DataSourceDTO dataSourceDTO = new DataSourceDTO();

        dataSourceDTO.setUrl((String) claims.get("url"));
        dataSourceDTO.setUsername((String) claims.get("username"));
        dataSourceDTO.setPassword((String) claims.get("password"));
        
        DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
        
        System.out.println("dataSource: " + dataSource);
        
        repository = new JsonObjectRepository(dataSource);
        
        JSONObject jsonList = repository.getJson(query);
        
        return jsonList;
    }
	
	/*public JSONObject getJson (String query, HttpServletRequest request) throws UnsupportedEncodingException {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);
		
		repository = new JsonObjectRepository(dataSource);
		
		JSONObject jsonList = repository.getJson(query);
		
		return jsonList;
	}*/
}





































