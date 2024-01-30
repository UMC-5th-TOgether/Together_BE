package com.backend.together.global.enums;

public enum PostStatus {
    ING, END;

    public static PostStatus fromString(String value) {
        for (PostStatus status : PostStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value);
    }
}
