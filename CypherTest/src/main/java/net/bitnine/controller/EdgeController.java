package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.bitnine.domain.Edge;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.EdgeRepository;
import net.bitnine.service.DatabaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class EdgeController {
	EdgeRepository edgeRepository;

	@Autowired private DatabaseService databaseService;

	@RequestMapping("/findEdge")
	public void findEdge(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
//	public JSONObject findEdge(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
//	public Map<String, Object> findEdge(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
//	public String findEdge(DataSourceDTO dataSourceDTO) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		edgeRepository = new EdgeRepository(dataSource);		

		List<Edge> edgeList = edgeRepository.findEdge();

		createJson2(edgeList, response);
//		createJson5(edgeList, response);
//		return createJson(edgeList, response);
//		return createJson3(edgeList);
//		return createJson4(edgeList, response);
	}
	
	private JSONObject createJson(List<Edge> edgeList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", edgeList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();

		for (Edge edge : edgeList) {
			JSONObject jsonObj = new JSONObject();

			jsonObj.put("rId", edge.getrId());
			jsonObj.put("rHead", edge.getrHead());
			jsonObj.put("rTail", edge.getrTail());
			jsonObj.put("rProps", edge.getrProps());
			jsonObj.put("pId", edge.getpId());
			jsonObj.put("pName", edge.getpName());
			jsonObj.put("pProps", edge.getpProps());
			
			jsonRowArray.add(jsonObj);
		}
		jsonObject.put("Rows", jsonRowArray);
		
		return jsonObject;
	}
	
	private void createJson2(List<Edge> edgeList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", edgeList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();

		for (Edge edge : edgeList) {
			JSONObject jsonObj = new JSONObject();

			jsonObj.put("rId", edge.getrId());
			jsonObj.put("rHead", edge.getrHead());
			jsonObj.put("rTail", edge.getrTail());
			jsonObj.put("rProps", edge.getrProps());
			jsonObj.put("pId", edge.getpId());
			jsonObj.put("pName", edge.getpName());
			jsonObj.put("pProps", edge.getpProps());
			
			jsonRowArray.add(jsonObj);
		}
		jsonObject.put("Rows", jsonRowArray);
		
		ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//private String createJson3(List<Edge> edgeList, HttpServletResponse response) {
	private String createJson3(List<Edge> edgeList) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", edgeList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Edge edge : edgeList) {
			Map<String, Object> m = new HashMap<>();

			m.put("rId", edge.getrId());
			m.put("rHead", edge.getrHead());
			m.put("rTail", edge.getrTail());
			m.put("rProps", edge.getrProps());
			m.put("pId", edge.getpId());
			m.put("pName", edge.getpName());
			m.put("pProps", edge.getpProps());
			
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
	

	
	private Map<String, Object> createJson4(List<Edge> edgeList, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", edgeList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Edge edge : edgeList) {
			Map<String, Object> m = new HashMap<>();

			m.put("rId", edge.getrId());
			m.put("rHead", edge.getrHead());
			m.put("rTail", edge.getrTail());
			m.put("rProps", edge.getrProps());
			m.put("pId", edge.getpId());
			m.put("pName", edge.getpName());
			m.put("pProps", edge.getpProps());
			
			objList.add(m);
		}
		map.put("Rows", objList);
		
		return map;
	}
	
	private void createJson5(List<Edge> edgeList, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", edgeList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Edge edge : edgeList) {
			Map<String, Object> m = new HashMap<>();

			m.put("rId", edge.getrId());
			m.put("rHead", edge.getrHead());
			m.put("rTail", edge.getrTail());
			m.put("rProps", edge.getrProps());
			m.put("pId", edge.getpId());
			m.put("pName", edge.getpName());
			m.put("pProps", edge.getpProps());
			
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
}
