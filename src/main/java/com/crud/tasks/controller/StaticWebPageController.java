package com.crud.tasks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class StaticWebPageController {

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        model.put("variable", "My Thymeleaf variable");
        model.put("line1", "2 * 2 = ");
        model.put("line2", "2 * 2 + 2 = ");
        model.put("line3", "2 - 2 * 2 = ");
        model.put("2", 2);

        return "index";
    }
}