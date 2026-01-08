package com.random.number.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.random.number.service.SQLExecutionService;

@RestController
@RequestMapping("/api/sql")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:3001")
public class SQLController {

    @Autowired
    private SQLExecutionService service;

    @PostMapping(value = "/execute", consumes = "text/plain")
    public String execute(@RequestBody String sql) throws Exception {
        return service.processSql(sql);
    }
}

