package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bitnine.domain.General;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.GeneralRepository;

@Service
public class GeneralService {
	GeneralRepository generalRepository;

	public List<General> findGeneral (String query, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);
		
		generalRepository = new GeneralRepository(dataSource);
		
		List<General> generalList = generalRepository.findGeneral(query);
		
		return generalList;
	}

	/*public List<Map<String, Object>> findGeneral (String query, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);
		
		generalRepository = new GeneralRepository(dataSource);
		
		List<Map<String, Object>> generalList = generalRepository.findGeneral(query);
		
		return generalList;
	}*/
	
	/*@Autowired private DatabaseService databaseService;
	public List<General> findGeneral (DataSourceDTO dataSourceDTO) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		generalRepository = new GeneralRepository(dataSource);
		
		List<General> generalList = generalRepository.findGeneral();
		
		return generalList;
	}*/
	
	

	//@Autowired private DatabaseService databaseService;
	
	
	
	

	public JSONObject createJson(List<General> generalList) {
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

















