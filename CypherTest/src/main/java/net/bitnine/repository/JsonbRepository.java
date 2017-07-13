package net.bitnine.repository;

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

import net.bitnine.domain.Path;
import net.bitnine.utils.MetaDataUtils;
import net.bitnine.domain.DataMeta;
import net.bitnine.domain.Jsonb;

@Repository
public class JsonbRepository {
	
	private JdbcTemplate jdbcTemplate;

	public JsonbRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
    
	String query = "match (a:production) where a.production_year::int > 2000 return a.production_year::numeric as prod_year, properties(a) as jsons limit 10";
	
    /*@Transactional(readOnly=true)
    public List<Jsonb> findJsonb(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(query, new JsonbRowMapper());
    }*/
    
    @Transactional(readOnly=true)
    public List<Jsonb> findJsonb() {
        return jdbcTemplate.query(query, new JsonbRowMapper());
    }
}
class JsonbRowMapper implements RowMapper<Jsonb>
{
    @Override
    public Jsonb mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Jsonb jsonb = new Jsonb();
    	jsonb.setProdYear(rs.getString("prod_year"));
    	jsonb.setJsons(rs.getString("jsons"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	jsonb.setDataMetaList(dataMetaList);
 
        return jsonb;
    }
}
