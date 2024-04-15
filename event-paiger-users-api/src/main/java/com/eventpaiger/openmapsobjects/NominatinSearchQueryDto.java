package com.eventpaiger.openmapsobjects;

public record NominatinSearchQueryDto(
        String street,
        String city,
        String county,
        String state,
        String country,
        String postalCode
) {
}
