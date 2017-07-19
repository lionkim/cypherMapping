package net.bitnine.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

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
import net.bitnine.domain.Vertex;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class JsonObjectRepository {
	
	private JdbcTemplate jdbcTemplate;

	public JsonObjectRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	public JSONObject getJson(String query) {
		return jdbcTemplate.query(query, new ResultSetExtractor<JSONObject>() {
			@Override
			public JSONObject extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				JSONObject mapRet = new JSONObject();

		    	ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

		    	List<DataMeta> dataMetaList = MetaDataUtils.getMetaDataList (resultSetMetaData);
		    	
		    	mapRet.put("meta", dataMetaList);

				JSONArray nodeJsonArr = new JSONArray();	// 파싱된 값 전체를 담을 배열
				
		    	int colCnt = resultSetMetaData.getColumnCount();
				while (resultSet.next()) {
					// ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해
					JSONObject rowJsonObject = new JSONObject();
					
					for (int count = 1; count <= colCnt; count++) { // i가 1부터 시작함에 유의		
						try {
							setChangeType(resultSet, resultSetMetaData, rowJsonObject, count);		// 컬럼의 타입별로 해당타입으로 변환하여 jsonObject에 put.
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
	
	private void setChangeType(ResultSet resultSet, ResultSetMetaData resultSetMetaData, JSONObject rowJsonObject, int cnt) throws ParseException, SQLException {
		JSONParser parser = new JSONParser();
		String columnTypeName = resultSetMetaData.getColumnTypeName(cnt);

		String columnName = resultSetMetaData.getColumnLabel(cnt).toUpperCase();
		String result = resultSet.getString(columnName);	
				
		switch (columnTypeName) {

		// number에는 int, long, double, float 등
		case "number": 
			long longResult = Long.parseLong(result);
			rowJsonObject.put(columnName, longResult );
			break;

		case "varchar":
			rowJsonObject.put(columnName, result );
			break;
			
		case "graphid": 
			Double doubleResult = resultSet.getDouble(columnName);
				
			rowJsonObject.put(columnName, doubleResult );
			break;
			
		case "text": 
			rowJsonObject.put(columnName, result );
			break;

		case "jsonb": 
			rowJsonObject.put(columnName,  (JSONObject) parser.parse(result)  );
			break;
			
		case "graphpath": 
			rowJsonObject.put(columnName,  (JSONObject) parser.parse(result)  );
			break;
			
		case "vertex": 
			rowJsonObject.put(columnName,  new Vertex() );
			break;
			
		/*

		case "EDGE": 
			return  new Long(result);

		case "PATH": 
			return  new Long(result);*/

		default : 
			rowJsonObject.put(columnName, result );
			break;
			//return result;

		}
	}


//	String query = "match ()-[a:keyword_of]-() retur n a limit 20";
	/*String query = "match (n:production)-[r:actress_in]-(p:person)"
			+ " where id(n) = '4.1388625'"
			+ " return id(r) as r_id, start(r) as r_head, \"end\"(r) as r_tail, properties(r) as r_props"
			+ " , id(p) as p_id, p.name as p_name, properties(p) as p_props";*/
	



}
