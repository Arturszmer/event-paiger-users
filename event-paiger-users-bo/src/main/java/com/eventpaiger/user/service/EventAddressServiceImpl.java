package com.eventpaiger.user.service;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAddressServiceImpl implements EventAddressService {

    private final GeocodingService geocodingService;
    private final EventAddressRepository eventAddressRepository;
    private final SecurityContextUsers securityContextHelper;

    @Override
    public UserProfileWithEventAddressesDto updateEventAddresses(List<EventAddressDto> updatedAddresses){

        UserProfile userProfile = securityContextHelper.getUserProfileFromAuthentication();
        UUID eventOrganizerId = userProfile.getEventOrganizerId();

        Set<String> addressesNames = validEventAddressUniqueness(updatedAddresses);
        List<EventAddress> allEventAddresses = eventAddressRepository.findAllByEventOrganizerId(eventOrganizerId);
        if(allEventAddresses.isEmpty()){
            List<EventAddressDto> list = saveAddresses(eventOrganizerId, updatedAddresses).stream()
                    .map(EventAddressAssembler::toDto)
                    .toList();
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile), list);
        } else {
            List<EventAddressDto> newUniqueAddresses = removeNotUniqueAddresses(updatedAddresses, addressesNames);
            List<EventAddressDto> list = saveAddresses(eventOrganizerId, newUniqueAddresses).stream()
                    .map(EventAddressAssembler::toDto)
                    .toList();
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile), list);
        }
    }

    private Set<String> validEventAddressUniqueness(List<EventAddressDto> eventAddressDtos) {
        Set<String> addressesNames = new HashSet<>();
        for(EventAddressDto dto : eventAddressDtos){
            if(!addressesNames.add(dto.customUserAddressName())){
                throw new IllegalArgumentException("Duplicate address name: " + dto.customUserAddressName());
            }
        }
        return addressesNames;
    }

    @NotNull
    private static List<EventAddressDto> removeNotUniqueAddresses(List<EventAddressDto> updatedAddresses, Set<String> addressesNames) {
        return updatedAddresses.stream()
                .filter(address -> addressesNames.stream().noneMatch(existAddress ->
                        address.customUserAddressName().equals(existAddress)))
                .toList();
    }

    private List<EventAddress> saveAddresses(UUID eventOrganizerId, List<EventAddressDto> eventAddressDtos) {
        List<EventAddress> geocodedAddresses = eventAddressDtos.stream()
                .map(dto -> {
                    NominatinSearchResponse nominatinSearchResponse =
                            buildFromGeocoding(EventAddressAssembler.toNominatinSearchQueryDto(dto));

                    if(nominatinSearchResponse == null || nominatinSearchResponse.getPlaceId() == null) {
                        log.warn("Invalid address: {}", dto);
                        return null;
                    }
                    log.info("Nomination search successfully, place id: {}", nominatinSearchResponse.getPlaceId());

                    return EventAddress.create(dto, eventOrganizerId, nominatinSearchResponse);
                })
                .filter(Objects::nonNull)
                .toList();
        return eventAddressRepository.saveAll(geocodedAddresses);
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
