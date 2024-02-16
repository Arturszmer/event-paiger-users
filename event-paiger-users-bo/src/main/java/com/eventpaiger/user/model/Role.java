package com.eventpaiger.user.model;

import com.eventpaiger.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity<Long> {

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleType name;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private List<Permission> permissions = new ArrayList<>();

    public Role(RoleType name) {
        this.name = name;
        if(name != RoleType.OBSERVER){
            permissions.addAll(List.of(
                    Permission.EVENT_UPDATE,
                    Permission.EVENT_DELETE,
                    Permission.EVENT_CREATE));
        }
    }
}
