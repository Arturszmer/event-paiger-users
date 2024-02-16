package com.eventpaiger.user.service;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setup(){
        userService = new UserService();
    }

    @Test
    public void should_return_false() {
        // given
        assertTrue(userService.changePassword());
        // git

        // when

        // then

    }

}
