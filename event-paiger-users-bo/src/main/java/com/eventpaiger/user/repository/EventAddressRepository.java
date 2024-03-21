package com.eventpaiger.user.repository;

import com.eventpaiger.user.model.event.EventAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventAddressRepository extends JpaRepository<EventAddress, Long> {

    List<EventAddress> findAllByEventOrganizerId(UUID eventOrganizerId);
}
