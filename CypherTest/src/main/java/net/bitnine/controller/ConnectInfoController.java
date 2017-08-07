package net.bitnine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.jwt.ConnectionInfoMap;

/**
 * 관리자가 사용자들의 접속 및 쿼리 정보를 조회하는 컨트롤러
 * @Author  : 김형우
 * @Date	  : 2017. 7. 29.
 *
 */
@RestController
public class ConnectInfoController {

    @Autowired private ConnectionInfoMap connectionInfoMap;
    
    @RequestMapping(value="/api/v1/db/connectInfo", method=RequestMethod.POST)
    public ConnectionInfoMap getConnectInfos() {        
        return connectionInfoMap;
    }
}
