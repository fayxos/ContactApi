package com.projects.contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpirationType {

    ONETIME(0),
    DAY(1),
    WEEK(2),
    MONTH(3),
    NEVER(4);

    private final int number;

}
