package net.bitnine.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.General;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.GeneralRepository;
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
	}
	
}






























