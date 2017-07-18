package net.bitnine.controller;



import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.service.DatabaseService;

/**
 * 사용자가 전송한 정보로 dataSource를 만드는 컨트롤러
 * 세션에 dataSource를 저장, 해제 하는 부분은 interceptor에서 처리
 * @author cppco
 *
 */
@RestController
public class DataSourceController {

	
	public static final String DATASOURCE = "dataSource";
	
	@Autowired private DatabaseService databaseService;
	
	/*@RequestMapping("/dbConnect")
	@ResponseBody
	public String dbConnect(DataSourceDTO dataSourceDTO, HttpSession session, Model model)  {

		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		System.out.println("dataSource: " + dataSource);
		
		model.addAttribute(DATASOURCE, dataSource);
		
//		return "/";
		return "DbConnect Success";
	}*/
	
	@RequestMapping("/dbConnect")
	public String dbConnect(DataSourceDTO dataSourceDTO, HttpSession session, Model model)  {

		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		System.out.println("dataSource: " + dataSource);
		
		if (dataSource != null) {
			session.setAttribute(DATASOURCE, dataSource);
			return "DbConnect Success";
		}
		return "DbConnect Failed";
	}
	
	@RequestMapping("/dbConnectOut")
	public String findGeneral(HttpSession session, Model model)  {
		
		if (session.getAttribute(DATASOURCE) != null) {
			session.removeAttribute(DATASOURCE);
			return "DbConnectOut Success";
		}

		return "DbConnectOut Failed";
	}
}



























