package net.bitnine.service;

import static net.bitnine.controller.DataSourceController.DATASOURCE;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bitnine.domain.Path;
import net.bitnine.domain.dto.DataSourceDTO;
import net.bitnine.repository.PathRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class PathService {
	PathRepository pathRepository;

	public List<Path> findPath (String query, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session.getAttribute(DATASOURCE) == null) {
			return null;
		}
		DataSource dataSource = (DataSource) session.getAttribute(DATASOURCE);
		System.out.println("dataSource: " + dataSource);

		pathRepository = new PathRepository(dataSource);

		List<Path> pathList = pathRepository.findPath(query);
		
		return pathList;
	}
	
	public JSONObject createJson (List<Path> pathList) {
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
}


























