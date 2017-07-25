package net.bitnine.controller;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.service.DatabaseService;

/**
 * 사용자가 전송한 정보로 dataSource를 만드는 컨트롤러
 * 세션에 dataSource를 저장, 해제
 * @author cppco
 *
 */
@RequestMapping("/api/v1/db/")
@RestController
public class DataSourceController {
	
	public static final String DATASOURCE = "dataSource";
	
	@Autowired private DatabaseService databaseService;
	
	@RequestMapping("/connect")
	public String connect(DataSourceDTO dataSourceDTO, HttpSession session, Model model)  {

		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
//		System.out.println("dataSource: " + dataSource);
		
		if (dataSource != null) {
			session.setAttribute(DATASOURCE, dataSource);
			return "Database Connect Success";
		}
		return "Database Connect Failed";
	}
	
	@RequestMapping("/disconnect")
	public String disConnect(HttpSession session, Model model)  {
		
		if (session.getAttribute(DATASOURCE) != null) {
			session.removeAttribute(DATASOURCE);
			return "Database DisConnect Success";
		}

		return "Database DisConnect Failed";
	}
}



























