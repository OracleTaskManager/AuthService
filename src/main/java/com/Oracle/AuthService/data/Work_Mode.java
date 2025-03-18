package com.Oracle.AuthService.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Work_Mode {
    ON_SITE("On Site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private final String displayName;

    Work_Mode(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Work_Mode fromString(String value) {
        for (Work_Mode mode : Work_Mode.values()) {
            if (mode.getDisplayName().equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid Work Mode: " + value);
    }

}
