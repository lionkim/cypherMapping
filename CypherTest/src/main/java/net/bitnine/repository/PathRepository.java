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
import net.bitnine.domain.Path;
import net.bitnine.utils.MetaDataUtils;

@Repository
public class PathRepository {
	
	private JdbcTemplate jdbcTemplate;

	public PathRepository(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

//	String query = "match path=(a:production {'title': 'Haunted House'})-[]-(b:company {'name': 'Ludo Studio'}) return path limit 10";

	String query = "match path=(a:production)-[]-(b:company)"
			+ " where id(a) = '4.7058'"
			+ " return nodes(path) as NODES, edges(path) as EDGES, id( (nodes(path))[1] ) as HEAD,"
			+ " id( (nodes(path))[length(path)+1] ) as TAIL";
	
	
    @Transactional(readOnly=true)
    public List<Path> findPath() {
        return jdbcTemplate.query(query, new PathRowMapper());
    }
}
class PathRowMapper implements RowMapper<Path>
{
	@Override
    public Path mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Path path = new Path();

    	path.setHead(rs.getString("head"));
    	path.setTail(rs.getString("tail"));
    	path.setNodes(rs.getString("nodes"));
    	path.setEdges(rs.getString("edges"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	path.setDataMetaList(dataMetaList);
 
        return path;
    }
	
    /*@Override
    public Path mapRow(ResultSet rs, int rowNum) throws SQLException 
    {
    	Path path = new Path();
        path.setProperties(rs.getString("path"));
    	
    	ResultSetMetaData data = rs.getMetaData();

    	List<DataMeta> dataMetaList = MetaDataUtils.setMetaData(data);
    	path.setDataMetaList(dataMetaList);
 
        return path;
    }*/
}
