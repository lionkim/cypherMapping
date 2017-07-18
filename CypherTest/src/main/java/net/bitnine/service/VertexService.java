package net.bitnine.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.bitnine.domain.General;
import net.bitnine.domain.Vertex;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.VertextRepository;

@Repository
public class VertexService {
	
	VertextRepository vertextRepository;
	
	@Autowired private DatabaseService databaseService;
	
	public List<Vertex> findVertex (DataSourceDTO dataSourceDTO) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		vertextRepository = new VertextRepository(dataSource);
		
		List<Vertex> vertexList = vertextRepository.findVertex();
		
		return vertexList;
	}
	
	/**
	 * vertexList로  json을 생성 
	 * @param vertexList
	 * @param response
	 * @return
	 */
	public JSONObject createJson (List<Vertex> vertexList, HttpServletResponse response) {
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
}
