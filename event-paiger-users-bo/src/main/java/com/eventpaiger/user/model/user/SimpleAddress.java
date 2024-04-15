package com.eventpaiger.user.model.user;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_address")
public class SimpleAddress extends BaseEntity<Long> {

    private String city;
    private String street;
    private String zipCode;
    private String houseNumber;
    private String apartmentNumber;


    public SimpleAddress(String city, String street, String zipCode, String houseNumber, String apartmentNumber) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    @Override
    public boolean equals(Object changedAddress) {
        if (this == changedAddress) return true;
        if (changedAddress == null || getClass() != changedAddress.getClass()) return false;
        SimpleAddress that = (SimpleAddress) changedAddress;
        return Objects.equals(city, that.city)
                && Objects.equals(street, that.street)
                && Objects.equals(zipCode, that.zipCode)
                && Objects.equals(houseNumber, that.houseNumber)
                && Objects.equals(apartmentNumber, that.apartmentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipCode, houseNumber, apartmentNumber);
    }
}

