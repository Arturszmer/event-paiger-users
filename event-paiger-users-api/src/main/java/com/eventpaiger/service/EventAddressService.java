package com.eventpaiger.service;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;

public interface EventAddressService {

    UserProfileWithEventAddressesDto updateEventAddresses(EventAddressDto eventAddressDtos);
}
