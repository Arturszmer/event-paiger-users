package com.eventpaiger.common;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EntityLog {

    @Column(name = "CREATED_BY", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "LAST_MODIFIED", updatable = true)
    @LastModifiedBy
    protected String modifiedBy;

    @Column(name = "CREATION_TIMESTAMP", updatable = false)
    @CreatedDate
    protected LocalDateTime creationTimestamp;

    @Column(name = "MODIFICATION_TIMESTAMP", updatable = true)
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
