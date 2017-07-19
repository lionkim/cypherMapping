package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
	JsonObjectRepository repository;

	
	public JSONObject getJson (String query, HttpServletRequest request) throws UnsupportedEncodingException {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);
		
		repository = new JsonObjectRepository(dataSource);
		
		JSONObject edgeList = repository.getJson(query);
		
		return edgeList;
	}
}





































