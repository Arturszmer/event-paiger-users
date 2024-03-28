package com.eventpaiger.user.service;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;
import com.eventpaiger.exception.EventAddressException;
import com.eventpaiger.openmapsobjects.NominatinSearchQueryDto;
import com.eventpaiger.openmapsobjects.NominatinSearchResponse;
import com.eventpaiger.service.EventAddressService;
import com.eventpaiger.user.assembler.EventAddressAssembler;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.model.event.EventAddress;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.EventAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.eventpaiger.user.helper.SecurityContextUsers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAddressServiceImpl implements EventAddressService {

    private final GeocodingService geocodingService;
    private final EventAddressRepository eventAddressRepository;
    private final SecurityContextUsers securityContextHelper;

    @Override
    public UserProfileWithEventAddressesDto updateEventAddresses(EventAddressDto updatedAddresses){

        UserProfile userProfile = securityContextHelper.getUserProfileFromAuthentication();
        UUID eventOrganizerId = userProfile.getEventOrganizerId();

        List<EventAddress> currentEventAddresses = eventAddressRepository.findAllByEventOrganizerId(eventOrganizerId);
        if(currentEventAddresses.isEmpty()){
            EventAddressDto savedAddress = startGeneratingGeocodingAddress(updatedAddresses, eventOrganizerId);
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile),
                    isEventAddressSavedSuccessfully(savedAddress));
        } else {
            Optional<EventAddress> toUpdateOpt = currentEventAddresses.stream()
                    .filter(addr -> updatedAddresses.customUserAddressName().equals(addr.getCustomUserAddressName()))
                    .findFirst();

            if(toUpdateOpt.isEmpty()){
                EventAddressDto eventAddressDto = startGeneratingGeocodingAddress(updatedAddresses, eventOrganizerId); // Zrobić metodę: GenerateNewAddress
                return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile),
                        isEventAddressSavedSuccessfully(eventAddressDto));
            }

            EventAddress toUpdateCurrentAddress = toUpdateOpt.get();
            // zrobić metodę updateExistingAddress
            EventAddressDto eventAddressDto = startGeneratingGeocodingAddress(updatedAddresses, toUpdateCurrentAddress);

            List<EventAddressDto> list = currentEventAddresses.stream()
                    .filter(addr -> !addr.getCustomUserAddressName().equals(eventAddressDto.customUserAddressName()))
                    .map(EventAddressAssembler::toDto)
                    .collect(Collectors.toList());
            list.add(eventAddressDto);

            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile), list);
        }
    }

    private EventAddressDto startGeneratingGeocodingAddress(EventAddressDto updatedAddresses, EventAddress toUpdateCurrentAddress) {
        return saveUpdatedAddress(updatedAddresses, toUpdateCurrentAddress)
                .map(addr -> EventAddressAssembler.toDto(eventAddressRepository.save(addr)))
                .orElseGet(() -> {
                    log.warn("Invalid address: {}", updatedAddresses);
                    return null;
                });
    }

    private Optional<EventAddress> saveUpdatedAddress(EventAddressDto updatedAddresses, EventAddress toUpdateCurrentAddress) {

        try {
            NominatinSearchResponse nominatinSearchResponse = getNominatinSearchResponse(updatedAddresses);
            toUpdateCurrentAddress.update(nominatinSearchResponse, updatedAddresses);
            log.info("Address has been updated successfully, new address {} is: {}",
                    updatedAddresses.customUserAddressName(), updatedAddresses.addressDto());
            return Optional.of(toUpdateCurrentAddress);
        } catch (EventAddressException e){
            return Optional.empty();
        }


    }

    @Nullable
    private EventAddressDto startGeneratingGeocodingAddress(EventAddressDto updatedAddresses, UUID eventOrganizerId) {
        return saveNewAddress(eventOrganizerId, updatedAddresses)
                .map(addr -> EventAddressAssembler.toDto(eventAddressRepository.save(addr)))
                .orElseGet(() -> {
                    log.warn("Invalid address: {}", updatedAddresses);
                    return null;
                });
    }

    @NotNull
    private static List<EventAddressDto> isEventAddressSavedSuccessfully(EventAddressDto savedAddress) {
        return savedAddress != null
                ? List.of(savedAddress)
                : Collections.emptyList();
    }

    private Optional<EventAddress> saveNewAddress(UUID eventOrganizerId, EventAddressDto eventAddressDto) {

        try {
            NominatinSearchResponse nominatinResponse = getNominatinSearchResponse(eventAddressDto);
            log.info("Nomination search successfully, place id: {}", nominatinResponse.getPlaceId());

            return Optional.of(EventAddress.create(eventAddressDto, eventOrganizerId, nominatinResponse));
        } catch (EventAddressException ex){
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
