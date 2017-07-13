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
import net.bitnine.domain.Vertex;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class VertextRepository {
	
	private JdbcTemplate jdbcTemplate;

	public VertextRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	String query = "match (a:production {'kind': 'episode'}) return a limit 20";
	
	String query = "match (n) where id(n) = '4.1388625' return id(n) as ID, label(n) as TYPE, n.title, properties(n) as props";
	
    @Transactional(readOnly=true)
    public List<Vertex> findVertex() {
        return jdbcTemplate.query(query, new VertexRowMapper());
    }
}
class VertexRowMapper implements RowMapper<Vertex>
{
    @Override
    public Vertex mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	
    	Vertex vertex = new Vertex();

    	vertex.setId(rs.getBigDecimal("id"));
    	vertex.setType(rs.getString("type"));
    	vertex.setTitle(rs.getString("title"));
    	vertex.setProps(rs.getString("props"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	vertex.setDataMetaList(dataMetaList);
 
        return vertex;
    }

    /*@Override
    public Vertex mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	
    	Vertex vertex = new Vertex();

    	vertex.setProps(rs.getString("a"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	vertex.setDataMetaList(dataMetaList);
 
        return vertex;
    }*/
}




























