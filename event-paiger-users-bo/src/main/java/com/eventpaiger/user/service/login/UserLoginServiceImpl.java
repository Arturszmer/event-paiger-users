package com.eventpaiger.user.service.login;

import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.model.UserProfile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Override
    public UserProfileDto hello() {
        UserProfile artek = new UserProfile("Artek", "artek@gmail.com", UUID.randomUUID());
        return UserProfileAssembler.toDto(artek);
    }
}
