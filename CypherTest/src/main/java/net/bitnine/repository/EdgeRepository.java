package net.bitnine.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Edge;
import net.bitnine.utils.MetaDataUtils;
import net.sf.json.JSONArray;

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
	
	public Map<String, Object> findEdge(String query) {
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
	}
}

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

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
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
