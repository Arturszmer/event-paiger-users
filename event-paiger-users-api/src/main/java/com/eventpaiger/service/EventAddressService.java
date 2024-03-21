package com.eventpaiger.service;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;

import java.util.List;

public interface EventAddressService {

    UserProfileWithEventAddressesDto updateEventAddresses(List<EventAddressDto> eventAddressDtos);
}
