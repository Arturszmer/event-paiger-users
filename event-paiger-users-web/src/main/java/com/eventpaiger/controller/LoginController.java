package com.eventpaiger.controller;

import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.user.service.login.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class LoginController {

    private final UserLoginService userLoginService;

    @GetMapping("/hello")
    ResponseEntity<UserProfileDto> hello(){
        UserProfileDto hello = userLoginService.hello();
        return ResponseEntity.ok(hello);
    }

    @PostMapping("/save")
    ResponseEntity<UserProfileDto> saveUser(@RequestBody UserProfileDto userProfileDto){
        return ResponseEntity.ok(userLoginService.saveUser(userProfileDto));
    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest){
//        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
//                loginRequest.username, loginRequest.password
//        );
//        this.authenticationManager.authenticate(authentication);
//        return ResponseEntity.ok().build();
//    }

    record LoginRequest(String username, String password){

    }

}
