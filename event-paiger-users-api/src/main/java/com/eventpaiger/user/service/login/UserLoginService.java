package com.eventpaiger.user.service.login;

import com.eventpaiger.dto.UserProfileDto;

public interface UserLoginService {

    UserProfileDto hello();
    UserProfileDto saveUser(UserProfileDto userProfileDto);
}
