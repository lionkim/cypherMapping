package net.bitnine.jwt;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.bitnine.domain.dto.DBConnectionInfo;

public class UserInfo {

	private String token;
	
	private DBConnectionInfo dbConnectionInfo;

	public UserInfo() {
	}

	public UserInfo(String token, DBConnectionInfo dbConnectionInfo) {
		this.token = token;
		this.dbConnectionInfo = dbConnectionInfo;
	}
		
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DBConnectionInfo getDbConnectionInfo() {
		return dbConnectionInfo;
	}

	public void setDbConnectionInfo(DBConnectionInfo dbConnectionInfo) {
		this.dbConnectionInfo = dbConnectionInfo;
	}
}
