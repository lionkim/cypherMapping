package net.bitnine.domain;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class ConnectInfo {
    
    private String token;
    
    private LocalDateTime connetTime;
    
    private int queryTimes;

    public ConnectInfo() {
    }

    public ConnectInfo(String token, LocalDateTime connetTime, int queryTimes) {
        this.token = token;
        this.connetTime = connetTime;
        this.queryTimes = queryTimes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getConnetTime() {
        return connetTime;
    }

    public void setConnetTime(LocalDateTime connetTime) {
        this.connetTime = connetTime;
    }

    public int getQueryTimes() {
        return queryTimes;
    }

    public void setQueryTimes(int queryTimes) {
        this.queryTimes = queryTimes;
    }
    
    
}
