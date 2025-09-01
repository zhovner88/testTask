package com.petstore.api.model.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PLACED("placed"),
    APPROVED("approved"), 
    DELIVERED("delivered");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}