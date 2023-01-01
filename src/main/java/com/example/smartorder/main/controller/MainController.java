package com.example.smartorder.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/error/denied")
    public String errorDenied(){

        return "error/denied";
    }

    @GetMapping("/admin/main.do")
    public String adminMain() {
        return "admin/main";
    }
}
