package com.eventpaiger.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ClosedFilter {

    @GetMapping(value = "/show")
    ResponseEntity<String> showMe(){
        return ResponseEntity.ok("SHOWED --> VERSION WITH GATEWAY: 2024-02-29 15:40");
    }

    @GetMapping("/organizer")
    @PreAuthorize("hasAnyRole('ORGANIZER')")
    ResponseEntity<String> showOrganizers(){
        return ResponseEntity.ok("You are an Organizer");
    }

    @GetMapping("/make-event")
    @PreAuthorize("hasAuthority('event:create')")
    ResponseEntity<String> createEvent(){
        return ResponseEntity.ok("You are ready to create an events");
    }
}
