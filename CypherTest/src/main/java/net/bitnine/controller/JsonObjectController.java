package net.bitnine.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.service.JsonObjectService;

@RestController
public class JsonObjectController {
	
	@Autowired private JsonObjectService service;

	@RequestMapping(value="/getJson",method=RequestMethod.POST)
	public JSONObject getJson(String query, HttpServletRequest request) throws  IOException {
			
		return service.getJson(query, request);
	}
	
}
