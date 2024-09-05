package com.projects.contact_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigType {

    PROFILE(0),
    PERSONAL(1);

    private final int number;
}
