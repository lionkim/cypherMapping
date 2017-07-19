package net.bitnine.repository;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Edge;
import net.bitnine.domain.Vertex;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class JsonObjectRepository {

	private JdbcTemplate jdbcTemplate;

	public JsonObjectRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public JSONObject getJson(String query) throws UnsupportedEncodingException {
	    query = "match path=(a:production)-[]-(b:company) where id(a) = '4.7058' return nodes(path) as NODES, edges(path) as EDGES, id( (nodes(path))[1] ) as HEAD, id( (nodes(path))[length(path)+1] ) as TAIL";


		return jdbcTemplate.query(query, new ResultSetExtractor<JSONObject>() {
			@Override
			public JSONObject extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				JSONObject mapRet = new JSONObject();

				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

				List<DataMeta> dataMetaList = MetaDataUtils.getMetaDataList(resultSetMetaData);

				mapRet.put("meta", dataMetaList);

				JSONArray nodeJsonArr = new JSONArray(); // 파싱된 값 전체를 담을 배열

				int colCnt = resultSetMetaData.getColumnCount();
				while (resultSet.next()) {
					// ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해
					JSONObject rowJsonObject = new JSONObject();

					for (int count = 1; count <= colCnt; count++) { // i가 1부터
																	// 시작함에 유의
						try {
							setChangeType(resultSet, resultSetMetaData, rowJsonObject, count); // 컬럼의 타입별로 해당타입으로 변환하여 jsonObject에 put.
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					nodeJsonArr.add(rowJsonObject);
				}

				mapRet.put("rows", nodeJsonArr);

				return mapRet;
			}
		});
	}

	private void setChangeType(ResultSet resultSet, ResultSetMetaData resultSetMetaData, JSONObject rowJsonObject,
			int cnt) throws ParseException, SQLException {
		JSONParser parser = new JSONParser();
		String columnTypeName = resultSetMetaData.getColumnTypeName(cnt);

		String columnName = resultSetMetaData.getColumnLabel(cnt).toUpperCase();
		String result = resultSet.getString(columnName);

		switch (columnTypeName) {

		// number에는 int, long, double, float 등
		case "number":
			long longResult = Long.parseLong(result);
			rowJsonObject.put(columnName, longResult);
			break;

		case "varchar":
			rowJsonObject.put(columnName, result);
			break;

		case "graphid":
			Double doubleResult = resultSet.getDouble(columnName);

			rowJsonObject.put(columnName, doubleResult);
			break;

		case "text":
			rowJsonObject.put(columnName, result);
			break;

		case "jsonb":
			rowJsonObject.put(columnName, (JSONObject) parser.parse(result));
			break;

		case "graphpath":
			rowJsonObject.put(columnName, (JSONObject) parser.parse(result));
			break;

        case "_vertex":
            List<Vertex> vertexList = createParsedVertext(result);
            rowJsonObject.put(columnName, vertexList);
            break;
            
        case "_edge":
            rowJsonObject.put(columnName, createParsedEdge(result));
            break;

		/*
		 * 
		 * case "EDGE": return new Long(result);
		 * 
		 * case "PATH": return new Long(result);
		 */

		default:
			rowJsonObject.put(columnName, result);
			break;
		// return result;

		}
	}
	
	private Edge createParsedEdge(String result) throws ParseException {
        // [distributed[8.965184][5.3494,4.7058]{}]
        // distributed[8.965184][5.3494,4.7058]{}
        // distributed[   8.965184][   5.3494,4.7058]{}

        String node = result.substring(1, result.length()-1);      // distributed[8.965184][5.3494,4.7058]{}
        String[] split = node.split("\\[");                             // [  distributed, 8.965184], 5.3494,4.7058]{}  ]
        String name = split[0];                                         // distributed
        String id = split[1].substring(0, split[1].length()-1);     // 8.965184

        String[] sources = split[2].split("\\]");                   // [  5.3494,4.7058,  {}  ]
        String[] sources1 = sources[0].split("\\,");             // [  5.3494, 4.7058  ]

        String source = sources1[0];                                // 5.3494
        String target = sources1[1];                                // 4.7058

        JSONParser parser = new JSONParser();
        JSONObject  props = (JSONObject) parser.parse(sources[1]);
        
        return new Edge (id, "Edge", name, source, target,  props);
	}

	public List<Vertex> createParsedVertext(String result) throws ParseException {

        String node = result.substring(1, result.length()-1);      // distributed[8.965184][5.3494,4.7058]{}
        
        String strPattern = "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]"; // nodes에서 파싱하기위한 정규식. ex) 'production[4.1111...]{' 을 검색함.

        Pattern pattern = Pattern.compile(strPattern);

        Matcher matcher = pattern.matcher(node);

        int[] matcherLocate = new int[10];
        
        // 정규 표현에 검색된 문자열 구하기
        // find() 메소드가 false 반환할 때까지 반복
        int cnt = 0;
        while (matcher.find()) {
            matcher.group(0);

            matcherLocate[cnt++] = matcher.start();       // 정규식으로 발견된 처음 위치
            matcherLocate[cnt++] = matcher.end();     // 정규식으로 발견된  끝 위치
        }
        matcherLocate[cnt] = node.length();

        String[] vertexes = new String[10];         // vertex들을 담을 배열, ex) { production[4.812332],  company[3.4444],  movie[4.444234], .... }
        
        for (int i = 0; i < cnt; i++) {
            
            int first = matcherLocate[i];
            int j = i+1;
            int second = matcherLocate[j];
            if ( ((i % 2) == 1) && (i != (cnt - 1)) ) {          // props 맨뒤의 ","를 없애기 위해
                second = second - 1;
            }
            vertexes [i] = node.substring(first, second);   
        }
        
        List<Vertex> vertextList = new ArrayList<>();

        JSONParser parser = new JSONParser();
        
        String[] vertexNames = new String[2];   
        
        for (int k = 0; k < cnt; k += 2) {              // vertex는 인덱스 2증가로 저장되있음.
            vertexNames = vertexes[k].split("\\[");     // ex) vertexes[k] = production[4.812332],   vertexNames = { production, 4.812332] }
            String name = vertexNames[0];
            String id = vertexNames[1].substring(0, vertexNames[1].length() - 1);       // 맨뒤에 "]"가 있으므로 전체길이에서 -1를 해줌
            int propsNum = k + 1;       // 해당 vertex의 props. 
            
            JSONObject  props = (JSONObject) parser.parse(vertexes[propsNum]);
            Vertex vertex = new Vertex(name, "Vertex", id, props);
            
            vertextList.add(vertex);
        }

        return vertextList;
    }

	public void getJsonData(String node) throws ParseException {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		
        jsonObject = (JSONObject) parser.parse(node);              
        
		List<String> arrNames = new ArrayList<>();
		
		Iterator<String> it = jsonObject.keySet().iterator();

		
		Map<String, Object> map = new HashMap<>();
		
		int i = 0;
		while (it.hasNext()) {
			String key = it.next();
			Object value = jsonObject.get(key);
			map.put(key, value);
			i++;
		}
		
		/*

		출처: http: // jgh6371.tistory.com/50 [Java Story]
		try {
			// JSONArray jArray = new JSONArray(result);

			Set key = map.keySet();
			for (int i = 0; i < jsonObject.size(); i++) {
				jsonObject.arrNames[i] = jsonObject.names(i);
			}
			for (int i = 0; i < ja.length(); ++i) {
				JSONObject jo = ja.getJSONObject(i);
				arrNames[i] = jo.names(i);
				Log.i("Connect->getJsonData", jo.getString("tableno"));
			}
		} catch (Exception e) {
			Log.e("ConnectToDatabase->getJsonData", "Error Parsing JSON Data " + e.toString());
		}*/
	}

	/*
	 * private Vertex parseVertext(String result) { String strPattern =
	 * "[a-zA-Z]*\\[[0-9]\\.[0-9]*\\]\\{"; // nodes에서 파싱하기위한 정규식. ex)
	 * 'production[4.1111...]{' 을 발견함.
	 * 
	 * Pattern pattern = Pattern.compile(strPattern);
	 * 
	 * Matcher matcher = pattern.matcher(result);
	 * 
	 * int[] matcherLocate = new int[4]; // nodes에 production과 company 2개가 있어서
	 * 처음과 마지막위치를 저장하기 위해 배열크기를 4로 함.
	 * 
	 * // 정규 표현에 검색된 문자열 구하기 // find() 메소드가 false 반환할 때까지 반복 int cnt = 0; while
	 * (matcher.find()) { matcher.group(0);
	 * 
	 * matcherLocate[cnt] = matcher.start(); // 정규식으로 발견된 처음 위치 cnt++;
	 * 
	 * matcherLocate[cnt] = matcher.end(); // 정규식으로 발견된 끝 위치 cnt++; } }
	 */

	// String query = "match ()-[a:keyword_of]-() retur n a limit 20";
	/*
	 * String query = "match (n:production)-[r:actress_in]-(p:person)" +
	 * " where id(n) = '4.1388625'" +
	 * " return id(r) as r_id, start(r) as r_head, \"end\"(r) as r_tail, properties(r) as r_props"
	 * + " , id(p) as p_id, p.name as p_name, properties(p) as p_props";
	 */

}