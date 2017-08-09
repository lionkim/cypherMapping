package net.bitnine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/api/v1/db/")
public class H2Controller {

    @RequestMapping(value="/h2", method=RequestMethod.GET)
    public String loginGet() {
        return "h2";
    }

    @RequestMapping(value="/h2", method=RequestMethod.POST)
    public String loginPost() {
        return "h2";
    }
}
