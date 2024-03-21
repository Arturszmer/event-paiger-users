package com.eventpaiger.controller;

import com.eventpaiger.user.service.GeocodingService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClosedFilter {

    private final GeocodingService geocodingService;

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

    @PostMapping("/geo")
    ResponseEntity<JsonObject> search(@RequestParam("query") String query){
        JsonObject location;
        try {
            location = geocodingService.search(query);
            return ResponseEntity.ok(location);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @PostMapping("/geo2")
//    ResponseEntity<List<EventAddress>> search(@RequestBody NominatinSearchQueryDto query){
//        try {
//            return ResponseEntity.ok(geocodingService.search(query));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
