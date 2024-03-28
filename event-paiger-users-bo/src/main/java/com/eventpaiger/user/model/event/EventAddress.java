package com.eventpaiger.user.model.event;

import com.eventpaiger.common.BaseEntity;
import com.eventpaiger.dto.EventAddressDto;
import com.eventpaiger.openmapsobjects.NominatinSearchResponse;
import com.eventpaiger.user.assembler.UserProfileAssembler;
import com.eventpaiger.user.model.user.SimpleAddress;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "event_address")
public class EventAddress extends BaseEntity<Long> {

        private String customUserAddressName;
        private UUID eventOrganizerId;
        private Long placeId;
        @OneToOne(cascade = CascadeType.ALL)
        private EventGeolocation eventGeolocation;
        //class name is returned from API
        private String classOfPlace;
        private String type;
        private String name;
        private String displayName;
        @OneToOne(cascade = CascadeType.ALL)
        private SimpleAddress address;

        EventAddress(String customUserAddressName,
                     UUID eventOrganizerId,
                     Long placeId,
                     EventGeolocation eventGeolocation,
                     String classOfPlace,
                     String type,
                     String name,
                     String displayName,
                     SimpleAddress address) {
                this.customUserAddressName = customUserAddressName;
                this.eventOrganizerId = eventOrganizerId;
                this.placeId = placeId;
                this.eventGeolocation = eventGeolocation;
                this.classOfPlace = classOfPlace;
                this.type = type;
                this.name = name;
                this.displayName = displayName;
                this.address = address;
        }

        public static EventAddress create(EventAddressDto dto, UUID eventOrganizerId, NominatinSearchResponse nominatinSearchResponse) {

                return new EventAddress(
                        dto.customUserAddressName(),
                        eventOrganizerId,
                        nominatinSearchResponse.getPlaceId(),
                        new EventGeolocation(nominatinSearchResponse.getLatitude(), nominatinSearchResponse.getLongitude()),
                        nominatinSearchResponse.getClazz(),
                        nominatinSearchResponse.getType(),
                        nominatinSearchResponse.getName(),
                        nominatinSearchResponse.getDisplayName(),
                        UserProfileAssembler.toEntity(dto.addressDto()));
        }

        public void update(NominatinSearchResponse nominatinSearchResponse, EventAddressDto updatedAddresses) {
                setPlaceId(nominatinSearchResponse.getPlaceId());
                setEventGeolocation(new EventGeolocation(nominatinSearchResponse.getLatitude(), nominatinSearchResponse.getLongitude()));
                setClassOfPlace(nominatinSearchResponse.getClazz());
                setType(nominatinSearchResponse.getType());
                setName(nominatinSearchResponse.getName());
                setDisplayName(nominatinSearchResponse.getDisplayName());
                setAddress(UserProfileAssembler.toEntity(updatedAddresses.addressDto()));
        }
}
