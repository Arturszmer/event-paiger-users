package com.eventpaiger.controller;

import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.user.service.login.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;

    @GetMapping("/hello")
    ResponseEntity<UserProfileDto> hello(){
        UserProfileDto hello = userLoginService.hello();
        return ResponseEntity.ok(hello);
    }
}
