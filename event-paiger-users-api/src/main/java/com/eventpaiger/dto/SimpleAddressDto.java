package com.eventpaiger.dto;

public record SimpleAddressDto(
        String city,
        String street,
        String zipCode,
        String houseNumber,
        String apartmentNumber
) {
}
