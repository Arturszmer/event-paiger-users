package com.eventpaiger.user.service.addressgenerator;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.exception.EventAddressException;
import com.eventpaiger.openmapsobjects.NominatinSearchQueryDto;
import com.eventpaiger.openmapsobjects.NominatinSearchResponse;
import com.eventpaiger.user.assembler.EventAddressAssembler;
import com.eventpaiger.user.model.event.EventAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAddressGeneratorService {

    private final GeocodingService geocodingService;

    public Optional<EventAddress> generateNewAddressFromGeocoding(UUID eventOrganizerId, EventAddressDto eventAddressDto) {

        try {
            NominatinSearchResponse nominatinResponse = getNominatinSearchResponse(eventAddressDto);
            log.info("Nomination search successfully, place id: {}", nominatinResponse.getPlaceId());

            return Optional.of(EventAddress.create(eventAddressDto, eventOrganizerId, nominatinResponse));
        } catch (EventAddressException ex){
            return Optional.empty();
        }
    }

    public Optional<EventAddress> generateUpdatedAddressFromGeocoding(EventAddressDto updatedAddress, EventAddress currentAddress){
        try {
            NominatinSearchResponse nominatinSearchResponse = getNominatinSearchResponse(updatedAddress);
            currentAddress.update(nominatinSearchResponse, updatedAddress);
            log.info("Address has been updated successfully, new address {} is: {}",
                    updatedAddress.customUserAddressName(), updatedAddress.addressDto());
             return Optional.of(currentAddress);
        } catch (EventAddressException e){
            return Optional.empty();
        }
    }

    @NotNull
    private NominatinSearchResponse getNominatinSearchResponse(EventAddressDto eventAddressDto) {
        NominatinSearchResponse nominatinResponse = buildFromGeocoding(EventAddressAssembler
                .toNominatinSearchQueryDto(eventAddressDto));

        if(nominatinResponse == null || nominatinResponse.getPlaceId() == null) {
            log.warn("Invalid address: {}", eventAddressDto);
            throw new EventAddressException("Invalid address!");
        }
        return nominatinResponse;
    }


    private NominatinSearchResponse buildFromGeocoding(NominatinSearchQueryDto address) {
        try {
            return geocodingService.getFirstResult(address);
        } catch (IOException ex){
            log.error("IOException occurred while searching for address: {}", ex.getMessage());
            return null;
        }
    }
}
