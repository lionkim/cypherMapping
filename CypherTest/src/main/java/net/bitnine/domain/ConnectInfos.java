package net.bitnine.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class ConnectInfos {
    
    private List<ConnectInfo> connectInfoList = new ArrayList<>();

    public ConnectInfos() {
    }

    public ConnectInfos(List<ConnectInfo> connectInfoList) {
        this.connectInfoList = connectInfoList;
    }

    
    public List<ConnectInfo> getConnectInfoList() {
        return connectInfoList;
    }

    public void setConnectInfoList(List<ConnectInfo> connectInfoList) {
        this.connectInfoList = connectInfoList;
    };
    
    
    
}
