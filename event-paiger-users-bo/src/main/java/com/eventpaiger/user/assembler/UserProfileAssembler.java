package com.eventpaiger.user.assembler;

import com.eventpaiger.dto.UserProfileDto;
import com.eventpaiger.user.model.UserProfile;

public class UserProfileAssembler {

    public static UserProfileDto toDto(UserProfile userProfile){
        return new UserProfileDto(
                userProfile.getUsername(),
                userProfile.getEmail(),
                userProfile.getEventOrganizerId());
    }

//    public static UserProfile toEntity(UserProfileDto dto){
//    }
}
