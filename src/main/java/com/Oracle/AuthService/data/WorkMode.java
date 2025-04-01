package com.Oracle.AuthService.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkMode {
    ON_SITE("On Site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    private final String displayName;

    WorkMode(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static WorkMode fromString(String value) {
        for (WorkMode mode : WorkMode.values()) {
            if (mode.getDisplayName().equalsIgnoreCase(value.trim())) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Invalid Work Mode: " + value);
    }

}
