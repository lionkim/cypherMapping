package net.bitnine.service;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.stereotype.Service;

import net.bitnine.domain.dto.DataSourceDTO;

@Service
public class DatabaseService {
	
	public DataSource createDataSource (DataSourceDTO dto) {
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(dto.getUrl());
		dataSource.setUsername(dto.getUsername());
		dataSource.setPassword(dto.getPassword());

        System.out.println("dataSource: " + dataSource);
		return dataSource;
	}
	
	/*public void createDataSource(DataSourceDTO dto) throws NamingException {
	    PGPoolingDataSource dataSource = new PGPoolingDataSource();
	    source.setDataSourceName("A Data Source");
	    source.setServerName("localhost");
	    source.setDatabaseName("test");
	    source.setUser("testuser");
	    source.setPassword("testpassword");
	    source.setMaxConnections(10);

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(dto.getUrl());
        dataSource.setUser(dto.getUsername());
        dataSource.setPassword(dto.getPassword());
        
	    new InitialContext().rebind("DataSource", source);
	}*/
}
