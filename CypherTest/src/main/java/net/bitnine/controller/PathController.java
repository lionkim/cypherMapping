package net.bitnine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import net.bitnine.domain.Path;
import net.bitnine.domain.Path;
import net.bitnine.domain.Path;
import net.bitnine.domain.Path;
import net.bitnine.domain.Jsonb;
import net.bitnine.domain.Path;
import net.bitnine.domain.Vertex;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.PathRepository;
import net.bitnine.service.DatabaseService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
public class PathController {
	PathRepository pathRepository;

	@Autowired private DatabaseService databaseService;

	@RequestMapping("/findPath")
	public JSONObject findPath(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
//	public String findPath(DataSourceDTO dataSourceDTO) {
//	public void findPath(DataSourceDTO dataSourceDTO, HttpServletResponse response) {
		DataSource dataSource = databaseService.createDataSource(dataSourceDTO);
		
		pathRepository = new PathRepository(dataSource);
		
		List<Path> pathList = pathRepository.findPath();

		//return createJson(pathList, response);
//		return createJson2(pathList);
		return createJson3(pathList, response);
	}

	
	private JSONObject createJson3(List<Path> pathList, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Meta", pathList.get(0).getDataMetaList());		// meta 데이터를 저장

		JSONArray jsonRowArray = new JSONArray();

		for (Path path : pathList) {
			JSONObject jsonObj = new JSONObject();
			
			String nodes =  path.getNodes();
			
			String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]\\{";		// nodes에서 파싱하기위한 정규식.  ex) 'production[4.1111...]{'  을 발견함.
			
			Pattern pattern = Pattern.compile(strPattern);
			
			Matcher matcher = pattern.matcher(nodes);

			int[] matcherLocate = new int[4];		// nodes에 production과 company 2개가 있어서 처음과 마지막위치를 저장하기 위해 배열크기를 4로 함.
			
			// 정규 표현에 검색된 문자열 구하기
			// find() 메소드가 false 반환할 때까지 반복
			int cnt = 0;
			while (matcher.find()) {
				matcher.group(0);

				matcherLocate[cnt] = matcher.start();		// 정규식으로 발견된 처음 위치
				cnt++;
			
				matcherLocate[cnt] = matcher.end();		// 정규식으로 발견된  끝 위치
				cnt++;
			}

			String node1 = nodes.substring(matcherLocate[0], matcherLocate[1]-2);		// production[4.1111...    '['로 분리시키려고
			String subStr2 = nodes.substring(matcherLocate[1]-1, matcherLocate[2]-1);	// production의 json
			String node2 = nodes.substring(matcherLocate[2], matcherLocate[3]-2);		// company[4.1111...    '['로 분리시키려고
			String subStr4 = nodes.substring(matcherLocate[3]-1, nodes.length()-1);		// company의 json
			
			String[] nodeArr1 = node1.split("\\[");			//  production, 4.1111... 로 분리 

			JSONArray nodeJsonArr = new JSONArray();	// 파싱된 값 전체를 담을 배열
			
			JSONObject nodeJson1 = new JSONObject();
			nodeJson1.put(nodeArr1[0], nodeArr1[1]);
			
			nodeJsonArr.add(nodeJson1);
			nodeJsonArr.add(subStr2);

			String[] nodeArr2 = node2.split("\\[");			//  company, 4.1111... 로 분리 
			JSONObject nodeJson2 = new JSONObject();
			nodeJson2.put(nodeArr2[0], nodeArr2[1]);
			
			nodeJsonArr.add(nodeJson2);
			nodeJsonArr.add(subStr4);
			
			jsonObj.put("head", path.getHead());
			jsonObj.put("tail", path.getTail());
			jsonObj.put("nodes", nodeJsonArr);
			jsonObj.put("edges", path.getEdges());
			
			jsonRowArray.add(jsonObj);
		}
		jsonObject.put("Rows", jsonRowArray);
		
		return jsonObject;
		
		/*ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), jsonMeta);		// json을 출력
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	private void createJson(List<Path> pathList, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", pathList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Path path : pathList) {
			Map<String, Object> m = new HashMap<>();
	    	
			m.put("head", path.getHead());
			m.put("tail", path.getTail());
			m.put("nodes", path.getNodes());
			m.put("edges", path.getEdges());
			
			objList.add(m);
		}
		map.put("Rows", objList);
		
		ObjectMapper om = new ObjectMapper();

		try {
			om.writeValue(response.getWriter(), map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String createJson2(List<Path> pathList) {
		Map<String, Object> map = new HashMap<>();
		
		map.put("Meta", pathList.get(0).getDataMetaList());
		
		List<Object> objList = new ArrayList<>();
		
		for (Path path : pathList) {
			Map<String, Object> m = new HashMap<>();

			m.put("head", path.getHead());
			m.put("tail", path.getTail());
			m.put("nodes", path.getNodes());
			m.put("edges", path.getEdges());
			
			objList.add(m);
		}
		map.put("Rows", objList);
		
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = "";
		try {
			json = ow.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}
