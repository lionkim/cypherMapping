package net.bitnine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/api/v1/db/")
public class LoginController {

    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String loginGet() {
        return "login";
    }

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginPost() {
        return "login";
    }
}
