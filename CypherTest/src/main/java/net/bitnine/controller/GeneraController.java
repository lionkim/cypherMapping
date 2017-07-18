package net.bitnine.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.General;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.GeneralRepository;
import net.bitnine.service.DatabaseService;
import net.bitnine.service.GeneralService;

@RestController
public class GeneraController {

	@Autowired private GeneralService service;

	/*@RequestMapping("/findGeneral")
	public JSONObject findGeneral(DataSourceDTO dataSourceDTO, HttpServletResponse response)  {
		
		List<General> generalList = service.findGeneral(dataSourceDTO);

		return service.createJson(generalList, response);
	}
	@RequestMapping("/findGeneral")
	public List<Map<String, Object>> findGeneral(String query, HttpServletRequest request, HttpServletResponse response)  {
		
		List<Map<String, Object>> generalList = service.findGeneral(query, request);
		
		return generalList;
	}
*/


	@RequestMapping(value="/findGeneral",method=RequestMethod.POST)
	public JSONObject findGeneral(String query, HttpServletRequest request)  {
		
		List<General> generalList = service.findGeneral(query, request);
		
		return service.createJson(generalList);
	}
}






























