package com.eventpaiger.dto;

public record EventAddressDto(
        SimpleAddressDto addressDto,
        String customUserAddressName
) {
}
