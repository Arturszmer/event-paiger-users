package com.eventpaiger.user.service.login;

import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.model.UserProfile;
import com.eventpaiger.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.eventpaiger.user.assembler.UserProfileAssembler.toDto;

@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {

    private final UserProfileRepository repository;

    @Override
    public UserProfileDto hello() {
        UserProfile artek = new UserProfile("Artur", "artek@gmail.com", UUID.randomUUID());
        return toDto(artek);
    }

    @Override
    public UserProfileDto saveUser(UserProfileDto userProfileDto) {
        UserProfile entity = UserProfileAssembler.toEntity(userProfileDto);
        return toDto(repository.save(entity));
    }
}
