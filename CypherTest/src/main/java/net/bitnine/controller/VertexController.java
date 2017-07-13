package net.bitnine.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.Vertex;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.VertextRepository;
import net.bitnine.service.DatabaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class VertexController {
	VertextRepository vertextRepository;

	@Autowired private DatabaseService databaseService;

	@RequestMapping("/findVertex")
	public JSONObject findVertex(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		vertextRepository = new VertextRepository(dataSource);
		
		List<Vertex> vertexList = vertextRepository.findVertex();

		return createJson(vertexList, response);
//		return createJsonBefore(vertexList, response);
	}

	/**
	 * vertexList로  json을 생성 
	 * @param vertexList
	 * @param response
	 * @return
	 */
	private JSONObject createJson (List<Vertex> vertexList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", vertexList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();

		for (Vertex vertex : vertexList) {
			JSONObject jsonObj = new JSONObject();

			jsonObj.put("id", vertex.getId());
			jsonObj.put("type", vertex.getType());
			jsonObj.put("title", vertex.getTitle());
			jsonObj.put("props", vertex.getProps());
			
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
	
	/**
	 * vertexList로  json을 생성 
	 * @param vertexList
	 * @param response
	 * @return
	 */
	private JSONObject createJsonBefore(List<Vertex> vertexList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", vertexList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();

		for (Vertex vertex : vertexList) {
			JSONObject jsonObj = new JSONObject();
			
			String properties = vertex.getProps();
			
			int index = properties.indexOf("]");
			
			String subStr = properties.substring(index+1, properties.length());

			jsonObj.put("properties", subStr);
			
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
