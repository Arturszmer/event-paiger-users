package com.eventpaiger.dto.userprofile;

import com.eventpaiger.dto.EventAddressDto;

import java.util.List;

public record UserProfileWithEventAddressesDto(
        UserProfileDto userProfileDto,
        List<EventAddressDto> eventAddressDtos
) {
}
