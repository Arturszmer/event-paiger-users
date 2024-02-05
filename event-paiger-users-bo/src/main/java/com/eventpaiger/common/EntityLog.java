package com.eventpaiger.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EntityLog {

    @Column(name = "CREATED_BY", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "LAST_MODIFIED")
    @LastModifiedBy
    protected String modifiedBy;

    @Column(name = "CREATION_TIMESTAMP", updatable = false)
    @CreatedDate
    protected LocalDateTime creationTimestamp;

    @Column(name = "MODIFICATION_TIMESTAMP")
    @LastModifiedDate
    protected LocalDateTime modificationTimestamp;
//
    @PrePersist
    public void prePersist(){
    // TODO: uzupełnić gdy będzie już implementacja Spring Security
        this.createdBy = "admin";
        this.modifiedBy = "admin";
        this.creationTimestamp = LocalDateTime.now();
        this.modificationTimestamp = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.modifiedBy = "admin";
        this.modificationTimestamp = LocalDateTime.now();
    }

}
