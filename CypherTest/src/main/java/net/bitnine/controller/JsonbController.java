package net.bitnine.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.Jsonb;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.JsonbRepository;
import net.bitnine.service.DatabaseService;

@RestController
public class JsonbController {
	JsonbRepository jsonbRepository;

	@Autowired private DatabaseService databaseService;
	
	@RequestMapping("/findJsonb")
	public JSONObject findJsonb(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		jsonbRepository = new JsonbRepository(dataSource);
		
		List<Jsonb> jsonbList = jsonbRepository.findJsonb();

		return createJson(jsonbList, response);
	}
	
	private JSONObject createJson(List<Jsonb> jsonbList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", jsonbList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();

		for (Jsonb jsonb : jsonbList) {
			JSONObject jsonObj = new JSONObject();

			jsonObj.put("prodYear", jsonb.getProdYear());
			jsonObj.put("jsons", jsonb.getJsons());
			
			jsonRowArray.add(jsonObj);
		}
		jsonObject.put("Rows", jsonRowArray);

		return jsonObject;
		/*ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), jsonObject);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
