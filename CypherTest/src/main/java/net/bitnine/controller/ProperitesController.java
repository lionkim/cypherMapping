package net.bitnine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.bitnine.service.PropertiesService;

@RestController
public class ProperitesController {

    @Autowired PropertiesService propertiesService;

    @RequestMapping(value="/api/v1/db/tokenValidTime", method=RequestMethod.POST)
    public String setMax(String tokenTime) {
        String max = propertiesService.getSetMax();
        
        propertiesService.setSetMax(tokenTime);
        
        if ( !tokenTime.equals(propertiesService.getSetMax()) ) {
            return "Set Max Failed";
        }
        return "Set Max Success";
    }
    
}
