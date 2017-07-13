package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bitnine.domain.dto.DataSourceDTO;

@RestController
public class JsonTestController {

	@RequestMapping("/testJson")
	public void findPath(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {

		Map<String, String> dummyData1 = new HashMap<>();
		dummyData1.put("value1", "값1");
		dummyData1.put("value2", "값2");
		 
		ObjectMapper om = new ObjectMapper();
		om.writeValue(response.getWriter(), dummyData1);
	}

	@RequestMapping("/listJson")
	public void listJson(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		// List 에 맵이 들어가 있는 형태
		List<Map<String, String>> dummyData2 = new ArrayList<>();
		
		Map<String, String> m = new HashMap<>();
		m.put("bonjovi", "singer");
		m.put("Hugh Jackman", "actor");
		m.put("Bill Nighy", "actor");
		m.put("Leonardo", "actor");
		dummyData2.add(m);
		 
		m = new HashMap<>();
		m.put("Bill Gates", "programmer");
		m.put("Lee", "developer");
		dummyData2.add(m);
		 
		ObjectMapper om = new ObjectMapper();
		 

		om.writeValue(response.getWriter(), dummyData2);
	}

}



























