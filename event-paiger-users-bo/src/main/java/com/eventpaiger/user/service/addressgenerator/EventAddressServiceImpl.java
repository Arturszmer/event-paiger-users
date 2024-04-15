package com.eventpaiger.user.service.addressgenerator;

import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.dto.userprofile.UserProfileWithEventAddressesDto;
import com.eventpaiger.service.EventAddressService;
import com.eventpaiger.user.assembler.EventAddressAssembler;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.helper.SecurityContextUsers;
import com.eventpaiger.user.model.event.EventAddress;
import com.eventpaiger.user.model.user.UserProfile;
import com.eventpaiger.user.repository.EventAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAddressServiceImpl implements EventAddressService {

    private final EventAddressRepository eventAddressRepository;
    private final SecurityContextUsers securityContextHelper;
    private final EventAddressGeneratorService generatorService;


    @Override
    public UserProfileWithEventAddressesDto updateEventAddresses(EventAddressDto updatedAddresses){
        UserProfile userProfile = securityContextHelper.getUserProfileFromAuthentication();
        UUID eventOrganizerId = userProfile.getEventOrganizerId();

        List<EventAddress> currentEventAddresses = eventAddressRepository.findAllByEventOrganizerId(eventOrganizerId);
        if(currentEventAddresses.isEmpty()){
            return handleFirstEventAddress(updatedAddresses, userProfile, eventOrganizerId);
        } else {
            return handleExistingAddress(updatedAddresses, userProfile, currentEventAddresses, eventOrganizerId);
        }
    }

    private UserProfileWithEventAddressesDto handleFirstEventAddress(EventAddressDto addressToUpdate,
                                                                     UserProfile userProfile,
                                                                     UUID eventOrganizerId){
        return generateNewAddressAndSave(addressToUpdate, userProfile, eventOrganizerId, new ArrayList<>());
    }

    private UserProfileWithEventAddressesDto handleExistingAddress(EventAddressDto addressToUpdate,
                                                                   UserProfile userProfile,
                                                                   List<EventAddress> currentEventAddresses,
                                                                   UUID eventOrganizerId){
        Optional<EventAddress> toUpdateOpt = findAddressToUpdate(addressToUpdate, currentEventAddresses);

        if(toUpdateOpt.isEmpty()){
            return generateNewAddressAndSave(addressToUpdate, userProfile, eventOrganizerId, currentEventAddresses);
        }

        Optional<EventAddress> eventAddressDtoOpt = generatorService.generateUpdatedAddressFromGeocoding(addressToUpdate, toUpdateOpt.get());
        if(eventAddressDtoOpt.isPresent()){
            EventAddress updatedAddress = eventAddressRepository.save(eventAddressDtoOpt.get());
            List<EventAddressDto> allUserEventAddressesDto = filterAndConvertToDto(currentEventAddresses, updatedAddress.getCustomUserAddressName());
            allUserEventAddressesDto.add(EventAddressAssembler.toDto(updatedAddress));

            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile), allUserEventAddressesDto);
        } else {
            log.warn("Invalid address: {}", addressToUpdate);
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile),
                    currentEventAddresses.stream()
                            .map(EventAddressAssembler::toDto)
                            .toList());
        }
    }

    @NotNull
    private UserProfileWithEventAddressesDto generateNewAddressAndSave(EventAddressDto updatedAddresses, UserProfile userProfile, UUID eventOrganizerId, List<EventAddress> currentEventAddresses) {
        Optional<EventAddress> eventAddress = generatorService.generateNewAddressFromGeocoding(eventOrganizerId, updatedAddresses);
        if(eventAddress.isPresent()){
            EventAddress updatedAddress = eventAddressRepository.save(eventAddress.get());
            currentEventAddresses.add(updatedAddress);
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile),
                    currentEventAddresses.stream()
                            .map(EventAddressAssembler::toDto)
                            .toList());
        } else {
            log.warn("Invalid address: {}", updatedAddresses);
            return new UserProfileWithEventAddressesDto(UserProfileAssembler.toDto(userProfile),
                    Collections.emptyList());
        }
    }

    private Optional<EventAddress> findAddressToUpdate(EventAddressDto updatedAddresses, List<EventAddress> currentEventAddresses){
        return currentEventAddresses.stream()
                .filter(addr -> updatedAddresses.customUserAddressName().equals(addr.getCustomUserAddressName()))
                .findFirst();
    }

    private List<EventAddressDto> filterAndConvertToDto(List<EventAddress> currentEventAddresses, String customUserAddressName){
        return currentEventAddresses.stream()
                .filter(addr -> !addr.getCustomUserAddressName().equals(customUserAddressName))
                .map(EventAddressAssembler::toDto)
                .collect(Collectors.toList());
    }
}
