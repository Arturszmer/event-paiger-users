package com.eventpaiger.user.assembler;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.SimpleAddressDto;
import com.eventpaiger.openmapsobjects.NominatinSearchQueryDto;
import com.eventpaiger.user.model.event.EventAddress;

public class EventAddressAssembler {

    public static NominatinSearchQueryDto toNominatinSearchQueryDto(EventAddressDto eventAddressDto){
        SimpleAddressDto dto = eventAddressDto.addressDto();
        return dto == null
                ? null
                : new NominatinSearchQueryDto(
                dto.street() + " " + dto.houseNumber(),
                dto.city(),
                null,
                null,
                "PL",
                dto.zipCode()
        );
    }

    public static EventAddressDto toDto(EventAddress eventAddress) {
        return new EventAddressDto(
                UserProfileAssembler.toDto(eventAddress.getAddress()),
                eventAddress.getCustomUserAddressName()
        );
    }
}
