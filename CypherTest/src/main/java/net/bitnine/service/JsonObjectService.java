package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bitnine.domain.Edge;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.JsonObjectRepository;

@Service
public class JsonObjectService {
	JsonObjectRepository edgeRepository;

	
	public Map<String, Object> getJson (String query, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);
		
		edgeRepository = new JsonObjectRepository(dataSource);
		
		Map<String, Object> edgeList = edgeRepository.getJson(query);
		
		return edgeList;
	}
	
	/**
	 * Meta와 Rows로 구분하여 Json을 생성
	 * @param edgeList
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public Map<String, Object> createJson (List<Edge> edgeList) throws JsonProcessingException, IOException {

		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", edgeList.get(0).getDataMetaList());		// edgeList의 DataMetaList를 Meta로 저장.
		
		List<Object> objList = new ArrayList<>();
		
		for (Edge edge : edgeList) {		// edge의 값들중 DataMetaList를 제외한 값들을 map에 저장하고 이 map은 list에 저장함.
			
			Map<String, Object> m = new HashMap<>();

			// props들이 문자열로 반환되므로 JsonNode타입으로 변환
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rPropsJson = mapper.readTree(edge.getrProps());
			JsonNode pPropsJson = mapper.readTree(edge.getpProps());
			
			m.put("rId", edge.getrId());
			m.put("rHead", edge.getrHead());
			m.put("rTail", edge.getrTail());
			m.put("rProps", rPropsJson);	
			m.put("pId", edge.getpId());
			m.put("pName", edge.getpName());
			m.put("pProps", pPropsJson);
			
			objList.add(m);
		}
		map.put("Rows", objList);		// 저장된 리스트는 Rows라는 키값으로 맵에 저장됨.

		return map;
	}
}





































