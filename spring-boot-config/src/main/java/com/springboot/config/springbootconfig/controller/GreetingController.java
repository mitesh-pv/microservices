package com.springboot.config.springbootconfig.controller;

import com.springboot.config.springbootconfig.model.DbSettings;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("#{${db.connection}}") // => SPEL-> spring expression language evaluator
    private Map<String, String> dbConf;

    @Autowired
    private DbSettings dbSettings;

    @GetMapping("/greeting")
    public String greeting() {
        return greetingMessage + " " + staticMessage + " welcome to " + appName + " " + appList + " "+dbConf;
    }

    @GetMapping("/db")
    public String dbSettings(){
        return dbSettings.getConnection() + " " + dbSettings.getHost();
    }
}
