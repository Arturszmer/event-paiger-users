package com.eventpaiger.user.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    EVENT_CREATE("event:create"),
    EVENT_UPDATE("event:update"),
    EVENT_DELETE("event:delete");

    private final String name;

}
