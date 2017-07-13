package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.bitnine.domain.Path;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.service.PathService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class PathController {

	@Autowired private PathService service;
	
	@RequestMapping("/findPath")
	public JSONObject findPath(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
//	public String findPath(DataSourceDTO dataSourceDTO) {
//	public void findPath(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
		
		List<Path> pathList = service.findPath(dataSourceDTO);
		
		//return createJson(pathList, response);
//		return createJson2(pathList);
		return service.createJson(pathList, response);
	}

	
	
	
	private void createJson(List<Path> pathList, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", pathList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Path path : pathList) {
			Map<String, Object> m = new HashMap<>();
	    	
			m.put("head", path.getHead());
			m.put("tail", path.getTail());
			m.put("nodes", path.getNodes());
			m.put("edges", path.getEdges());
			
			objList.add(m);
		}
		map.put("Rows", objList);
		
		ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String createJson2(List<Path> pathList) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", pathList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Path path : pathList) {
			Map<String, Object> m = new HashMap<>();

			m.put("head", path.getHead());
			m.put("tail", path.getTail());
			m.put("nodes", path.getNodes());
			m.put("edges", path.getEdges());
			
			objList.add(m);
		}
		map.put("Rows", objList);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}
