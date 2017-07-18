package net.bitnine.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Edge;
import net.bitnine.utils.MetaDataUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository
public class EdgeRepository {
	
	private JdbcTemplate jdbcTemplate;

	public EdgeRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	String query = "match ()-[a:keyword_of]-() retur n a limit 20";
	/*String query = "match (n:production)-[r:actress_in]-(p:person)"
			+ " where id(n) = '4.1388625'"
			+ " return id(r) as r_id, start(r) as r_head, \"end\"(r) as r_tail, properties(r) as r_props"
			+ " , id(p) as p_id, p.name as p_name, properties(p) as p_props";*/
	
    /*@Transactional(readOnly=true)
    public List<Edge> findEdge() {
        return jdbcTemplate.query(query, new EdgeRowMapper());
    }*/
    
    /*public List<Map<String, Object>> findEdge(String query) {
        return jdbcTemplate.queryForList(query);
    }*/
	/*public List<Map<String, Object>> findEdge(String query) {
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);

		int columnCount = rowSet.getMetaData().getColumnCount();
		System.out.println(columnCount);

		while (rowSet.next()) {

			for (int id = 1; id <= columnCount; id++) {
				System.out.println(rowSet.getString(id));
			}

		}
		return null;
	}*/
	
	public JSONObject findEdge(String query) {
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
					for (int i = 1; i <= colCnt; i++) { // i가 1부터 시작함에 유의
						String columnName = resultSetMetaData.getColumnLabel(i).toUpperCase();
						rowJsonObject.put(columnName, resultSet.getString(columnName));
					}
					nodeJsonArr.add(rowJsonObject);
				}

		    	mapRet.put("rows", nodeJsonArr);
		    	
				return mapRet;
			}
		});
	}
	

	/*public Map<String, Object> findEdge(String query) {
		return jdbcTemplate.query(query, new ResultSetExtractor<Map<String, Object>>() {
			@Override
			public Map<String, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				Map<String, Object> mapRet = new HashMap<>();

		    	ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

		    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(resultSetMetaData);
		    	
		    	mapRet.put("meta", dataMetaList);
		    	
		    	List<LinkedHashMap<String, String>> resultList = new ArrayList<>();
		    	
		    	int colCnt = resultSetMetaData.getColumnCount();
				while (resultSet.next()) {
					// ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해
					LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<String, String>();
					for (int i = 1; i <= colCnt; i++) { // i가 1부터 시작함에 유의
						String columnName = resultSetMetaData.getColumnLabel(i).toUpperCase();
						linkedHashMap.put(columnName, resultSet.getString(columnName));
					}
					resultList.add(linkedHashMap);
				}

		    	mapRet.put("rows", resultList);
		    	
				return mapRet;
			}
		});
	}*/
	
	
	/*public Map<String, Object> findEdge(String query) {
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(query);
		
		Map<String, Object> map = new HashMap<>();
		
		SqlRowSetMetaData data = rowSet.getMetaData();
//		ResultSetMetaData data = (ResultSetMetaData) rowSet.getMetaData();

    	List<DataMeta> dataMetaList = null;
		dataMetaList = MetaDataUtils.setSqlRowSetMetaData(data);
 
		map.put("Meta", dataMetaList);		// edgeList의 DataMetaList를 Meta로 저장.
				
//		List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
		int columnCount = rowSet.getMetaData().getColumnCount();
		System.out.println(columnCount);


		JSONArray jsonArray = new JSONArray();
		
		while (rowSet.next()) {
			for (int id = 1; id <= columnCount; id++) {
				System.out.println(rowSet.getString(id));
				jsonArray.add(rowSet.getString(id));
			}
		}
		

		map.put("Rows", jsonArray);		// 저장된 리스트는 Rows라는 키값으로 맵에 저장됨.
		
		return map;
	}*/
}

/*class Mapper implements RowMapper<Edge> {

	@Override
	public Edge mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		int colCnt = resultSetMetaData.getColumnCount();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(resultSetMetaData);
    	
		while (rs.next()) {
			// ResultSet의 결과를 LinkedHashMap에 저장 - put 순서대로 꺼내기 위해 HashMap대신
			// LinkedHashMap 사용
			// HashMap에 저장 후 Iteration으로 꺼내면 순서가 뒤죽박죽..
			LinkedHashMap lhm = new LinkedHashMap();
			for (int i = 1; i <= colCnt; i++) { // i가 1부터 시작함에 유의
				String columnName = resultSetMetaData.getColumnLabel(i).toUpperCase();
				lhm.put(columnName, rs.getString(columnName));
			}
			resultList.add(lhm);
		}
	}
	
}*/

class EdgeRowMapper implements RowMapper<Edge>
{
	@Override
    public Edge mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Edge edge = new Edge();

    	edge.setrId(rs.getBigDecimal("r_id"));
    	edge.setrHead(rs.getBigDecimal("r_head"));
    	edge.setrTail(rs.getBigDecimal("r_tail"));
    	edge.setrProps(rs.getString("r_props"));
    	edge.setpId(rs.getBigDecimal("p_id"));
    	edge.setpName(rs.getString("p_name"));
    	edge.setpProps(rs.getString("p_props"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.getMetaDataList(data);
    	edge.setDataMetaList(dataMetaList);
 
        return edge;
    }
	
	/*@Override
    public Edge mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Edge edge = new Edge();
    	edge.setrProps(rs.getString("a"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	edge.setDataMetaList(dataMetaList);
 
        return edge;
    }*/
}
