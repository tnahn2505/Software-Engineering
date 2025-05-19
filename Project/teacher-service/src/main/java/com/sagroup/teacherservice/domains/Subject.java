package com.sagroup.teacherservice.domains;

public enum Subject {
    TOAN("Toán"),
    LY("Lý"),
    HOA("Hóa"),
    VAN("Văn"),
    ANH("Anh");

    private final String displayName;

    Subject(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Subject fromString(String value) {
        try {
            return Subject.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid subject: " + value);
        }
    }
} 