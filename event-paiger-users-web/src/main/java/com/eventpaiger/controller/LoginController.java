package com.eventpaiger.controller;

import com.eventpaiger.authentication.AuthenticationService;
import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.dto.auth.AuthenticationRequest;
import com.eventpaiger.dto.auth.AuthenticationResponse;
import com.eventpaiger.dto.auth.RegistrationRequest;
import com.eventpaiger.user.service.login.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/auth")
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;
    private final AuthenticationService authService;

    @GetMapping("/hello")
    ResponseEntity<UserProfileDto> hello(){
        UserProfileDto hello = userLoginService.hello();
        return ResponseEntity.ok(hello);
    }

    @PostMapping("/save")
    ResponseEntity<UserProfileDto> saveUser(@RequestBody UserProfileDto userProfileDto){
        return ResponseEntity.ok(userLoginService.saveUser(userProfileDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }
}
