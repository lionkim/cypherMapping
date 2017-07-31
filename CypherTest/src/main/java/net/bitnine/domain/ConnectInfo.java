package net.bitnine.domain;

import java.time.LocalDateTime;

public class ConnectInfo {
    
    private String token;
    
//    private LocalDateTime connetTime;
    private String connetTime;
    
    private int queryTimes;
    
    private State state;

    public ConnectInfo() {
    }

    public ConnectInfo(String token, String connetTime, int queryTimes, State state) {
        this.token = token;
        this.connetTime = connetTime;
        this.queryTimes = queryTimes;
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConnetTime() {
        return connetTime;
    }

    public void setConnetTime(String connetTime) {
        this.connetTime = connetTime;
    }

    public int getQueryTimes() {
        return queryTimes;
    }

    public void setQueryTimes(int queryTimes) {
        this.queryTimes = queryTimes;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
    
    
}
