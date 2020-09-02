package com.cmmr.permission.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @RequestMapping("/index.html")
    public String index() {
        return "home";
    }

    @RequestMapping("/logout.html")
    public String logout() {
        return "login";
    }
}
