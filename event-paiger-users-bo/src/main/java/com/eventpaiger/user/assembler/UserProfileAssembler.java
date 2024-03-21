package com.eventpaiger.user.assembler;

import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileDetailsDto;
import com.eventpaiger.dto.userprofile.UserProfileDto;
import com.eventpaiger.user.model.user.SimpleAddress;
import com.eventpaiger.user.model.user.UserProfile;

import java.util.UUID;

public class UserProfileAssembler {

    public static UserProfileDto toDto(UserProfile userProfile){
        return new UserProfileDto(
                userProfile.getUsername(),
                userProfile.getEmail(),
                userProfile.getEventOrganizerId());
    }

    public static UserProfile toEntity(UserProfileDto userProfileDto) {
        return UserProfile.createForEvent(
                userProfileDto.username(), userProfileDto.email(), UUID.randomUUID()
        );
    }

    public static SimpleAddressDto toDto(SimpleAddress userAddress){
        return userAddress == null
                ? null
                : new SimpleAddressDto(
                userAddress.getCity(),
                userAddress.getStreet(),
                userAddress.getZipCode(),
                userAddress.getHouseNumber(),
                userAddress.getApartmentNumber()
        );
    }

    public static SimpleAddress toEntity(SimpleAddressDto userAddressDto){
        return userAddressDto == null
                ? null
                : new SimpleAddress(
                        userAddressDto.city(),
                        userAddressDto.street(),
                        userAddressDto.zipCode(),
                        userAddressDto.houseNumber(),
                        userAddressDto.apartmentNumber()
        );
    }

    public static UserProfileDetailsDto toDtoDetails(UserProfile savedUser) {
        return new UserProfileDetailsDto(
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getEventOrganizerId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                toDto(savedUser.getUserAddress())
        );
    }
}
