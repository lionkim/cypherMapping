package net.bitnine.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.exception.InvalidTokenException;
import net.bitnine.service.JsonObjectService;

/**
 * 사용자의 query요청을 처리하는 컨트롤러
 * @Author  : 김형우
 * @Date	  : 2017. 7. 25.
 *
 */
@RestController
public class JsonObjectController {

	@Autowired private JsonObjectService service;

	@RequestMapping(value="/api/v1/db/query", method=RequestMethod.POST)
	public JSONObject getJson(String query, @RequestHeader(value="Authorization") String Authorization) throws  IOException, InvalidTokenException {
	    
		return service.getJson(query, Authorization);
	}
	
}
