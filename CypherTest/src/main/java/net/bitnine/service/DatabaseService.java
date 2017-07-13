package net.bitnine.service;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.stereotype.Service;

import net.bitnine.domain.dto.DataSourceDTO;

@Service
public class DatabaseService {
	
	public DataSource createDataSource(DataSourceDTO dto) {
		BasicDataSource dataSource = new BasicDataSource();
		
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(dto.getUrl());
		dataSource.setUsername(dto.getUsername());
		dataSource.setPassword(dto.getPassword());
		return dataSource;
	}
}
