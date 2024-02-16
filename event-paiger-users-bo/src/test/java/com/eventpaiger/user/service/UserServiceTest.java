package com.eventpaiger.user.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService();
    }

    @Test
    public void should_return_false() {
        // given
        assertFalse(userService.changePassword());

        // when

        // then

    }

}
