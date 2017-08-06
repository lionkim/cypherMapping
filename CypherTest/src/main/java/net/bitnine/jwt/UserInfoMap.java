package net.bitnine.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.bitnine.domain.dto.DBConnectionInfo;

@Component
@Scope("application")
public class UserInfoMap {

	private Map<String, DBConnectionInfo> userInfos = new HashMap<>();

	public Map<String, DBConnectionInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(Map<String, DBConnectionInfo> userInfos) {
		this.userInfos = userInfos;
	}
}
