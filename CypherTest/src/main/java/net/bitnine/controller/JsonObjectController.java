package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.Edge;
import net.bitnine.repository.JsonObjectRepository;
import net.bitnine.service.JsonObjectService;

@RestController
public class JsonObjectController {
	JsonObjectRepository edgeRepository;


	@Autowired private JsonObjectService service;


	@RequestMapping(value="/getJson",method=RequestMethod.POST)
	public Map<String, Object> getJson(String query, HttpServletRequest request) throws  IOException {
	
		
		return service.getJson(query, request);
	}


	
	private JSONObject createJson(List<Edge> edgeList) {
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
		
		//return jsonObject;
		return ( jsonObject.isEmpty() ? null : jsonObject );
	}
	



}
