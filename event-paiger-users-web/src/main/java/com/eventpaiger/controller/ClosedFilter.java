package com.eventpaiger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ClosedFilter {

    @GetMapping(value = "/show")
    ResponseEntity<String> showMe(){
        return ResponseEntity.ok("SHOWED");
    }
}
