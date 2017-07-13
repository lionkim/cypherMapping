package net.bitnine.repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Edge;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class EdgeRepository {
	
	private JdbcTemplate jdbcTemplate;

	public EdgeRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	String query = "match ()-[a:keyword_of]-() return a limit 20";
	String query = "match (n:production)-[r:actress_in]-(p:person)"
			+ " where id(n) = '4.1388625'"
			+ " return id(r) as r_id, start(r) as r_head, \"end\"(r) as r_tail, properties(r) as r_props"
			+ " , id(p) as p_id, p.name as p_name, properties(p) as p_props";
			//+ " , id(p) as p_id, p.name as p_name, properties(p) as p_props limit 5";
	
    @Transactional(readOnly=true)
    public List<Edge> findEdge() {
        return jdbcTemplate.query(query, new EdgeRowMapper());
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
