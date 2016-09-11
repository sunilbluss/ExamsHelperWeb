package com.grudus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StaticController {


    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String mainPage() {
        return "main";
    }
}
