package net.bitnine.controller;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.domain.ConnectInfos;

@RestController
public class ConnectInfoController {

    @Autowired private ConnectInfos connectInfos;
    
    @RequestMapping(value="/api/v1/db/connectInfo", method=RequestMethod.POST)
    public ConnectInfos getConnectInfos() {        
        return connectInfos;
    }
    
    /*@RequestMapping(value="/api/v1/db/connectInfo", method=RequestMethod.POST)
    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        
        jsonObject.put("ConnectInfo", connectInfos); 
        
        return jsonObject;
    }*/
}
