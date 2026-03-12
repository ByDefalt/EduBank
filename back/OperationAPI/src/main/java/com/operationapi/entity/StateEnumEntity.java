package com.operationapi.entity;

public enum StateEnumEntity {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    FAILED("FAILED"),
    CANCELLED("CANCELLED");

    private final String value;

    StateEnumEntity(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static StateEnumEntity fromValue(String text) {
        for (StateEnumEntity state : StateEnumEntity.values()) {
            if (state.value.equals(text)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + text);
    }
}