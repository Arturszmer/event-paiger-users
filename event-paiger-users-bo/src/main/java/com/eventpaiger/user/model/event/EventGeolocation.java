package com.eventpaiger.user.model.event;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class EventGeolocation extends BaseEntity<Long>{

    private String latitude;
    private String longitude;

    public EventGeolocation(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
