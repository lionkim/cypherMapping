package net.bitnine.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.postgresql.util.PGobject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.bitnine.domain.DataMeta;
import net.bitnine.domain.General;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class GeneralRepository {
	
	private JdbcTemplate jdbcTemplate;

	public GeneralRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	String query = "match (a)<-[c:produced]-(b) return id(a) as id_a, label(a) as label_a, a.title, id(b) as id_b ,label(b) as label_b, b.name, label(c) as label_c,  id(c) as id_c  limit 10";
	
    @Transactional(readOnly=true)
	public List<General> findGeneral() {
        return jdbcTemplate.query(query, new GeneralRowMapper());
    }
}

class GeneralRowMapper implements RowMapper<General>
{
    @Override
    public General mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	General general = new General();

    	String idA = ((PGobject) rs.getObject("id_a")).getValue();
    	String idB = ((PGobject) rs.getObject("id_b")).getValue();
    	String idC = ((PGobject) rs.getObject("id_c")).getValue();
    			
    	general.setIdA(idA);
    	general.setLabelA(rs.getString("Label_a"));
    	general.setTitle(rs.getString("title"));
    	general.setIdB(idB);
    	general.setLabelB(rs.getString("Label_b"));
    	general.setName(rs.getString("name"));
    	general.setLabelC(rs.getString("Label_c")); 
    	general.setIdC(idC);
    	
    	ResultSetMetaData data = rs.getMetaData();
    	
    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
        general.setDataMetaList(dataMetaList);
        
    	return general;
    }
}




























