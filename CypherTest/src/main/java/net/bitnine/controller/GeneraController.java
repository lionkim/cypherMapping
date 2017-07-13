package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bitnine.domain.Path;
import net.bitnine.domain.Edge;
import net.bitnine.domain.General;
import net.bitnine.domain.Jsonb;
import net.bitnine.domain.Vertex;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.PathRepository;
import net.bitnine.repository.EdgeRepository;
import net.bitnine.repository.GeneralRepository;
import net.bitnine.repository.JsonbRepository;
import net.bitnine.repository.VertextRepository;
import net.bitnine.service.DatabaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class GeneraController {
	GeneralRepository generalRepository;

	@Autowired private DatabaseService databaseService;

	@RequestMapping("/findGeneral")
	public JSONObject findGeneral(DataSourceDTO dataSourceDTO, HttpServletResponse response)  {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);

		generalRepository = new GeneralRepository(dataSource);
		
		List<General> generalList = generalRepository.findGeneral();

		return createJson(generalList, response);
	}

	private JSONObject createJson(List<General> generalList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", generalList.get(0).getDataMetaList());

		JSONArray jsonRowArray = new JSONArray();
		
		for (General general : generalList) {
			JSONObject jsonObj = new JSONObject();

			jsonObj.put("idA", general.getIdA());
			jsonObj.put("labelA", general.getLabelA());
			jsonObj.put("title", general.getTitle());
			jsonObj.put("idB", general.getIdB());
			jsonObj.put("labelB", general.getLabelB());
			jsonObj.put("name", general.getName());
			jsonObj.put("labelC", general.getLabelC());
			jsonObj.put("idC", general.getIdC());
			
			jsonRowArray.add(jsonObj);
		}
		jsonObject.put("Rows", jsonRowArray);

		return jsonObject;
		/*ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), jsonMeta);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	
	
	
	private JSONArray createJson2(List<General> generalList) {
		JSONArray jsonArray = new JSONArray();


		JSONObject jsonObj2 = new JSONObject();
		jsonObj2.put("Meta", generalList.get(0).getDataMetaList());
		int i = 0;
		
		for (General general : generalList) {
			JSONObject jsonObj = new JSONObject();
			
			if (i == 0) {
				jsonObj.put("Meta", general.getDataMetaList());
			}
			
			general.setDataMetaList(null);
			
			jsonObj.put("Rows", general);
			
			jsonArray.add(jsonObj);
			i++;
		}
		return jsonArray;
	}
}






























