package net.bitnine.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.Vertex;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.VertextRepository;
import net.bitnine.service.DatabaseService;
import net.bitnine.service.VertexService;

@RestController
public class VertexController {

	@Autowired private VertexService service;

	@RequestMapping("/findVertex")
	public JSONObject findVertex(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
			
		List<Vertex> vertexList = service.findVertex(dataSourceDTO);

		return service.createJson(vertexList, response);
	}
}
