package com.eventpaiger.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record UserUpdateEventAddresses(
        List<EventAddressDto> organizerAddressesDto
) {
    public UserUpdateEventAddresses {
        organizerAddressesDto = Objects.requireNonNullElse(organizerAddressesDto, new ArrayList<>());
    }
}
