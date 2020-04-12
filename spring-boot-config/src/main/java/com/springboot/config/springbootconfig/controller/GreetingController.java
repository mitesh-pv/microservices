package com.springboot.config.springbootconfig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GreetingController {

    @Value("${app.message}")
    private String greetingMessage;

    @Value("static message")
    private String staticMessage;

    @Value("${app.name:default value}")
    private String appName;

    @Value("${app.list.values}")
    private List<String> appList;

    @Value("#{${app.db-values}}") // => SPEL-> spring expression language evaluator
    private Map<String, String> dbConf;

    @GetMapping("/greeting")
    public String greeting() {
        return greetingMessage + " " + staticMessage + " welcome to " + appName + " " + appList + " "+dbConf;
    }
}
