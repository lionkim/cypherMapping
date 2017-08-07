package net.bitnine.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.bitnine.jwt.ConnectInfo;

@Component
@Scope("application")
public class ConnectionInfoMap {

	private Map<String, ConnectInfo> userInfos = new HashMap<>();

	public Map<String, ConnectInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(Map<String, ConnectInfo> userInfos) {
		this.userInfos = userInfos;
	}
}
