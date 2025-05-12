package com.sagroup.studentservice.constants;

public enum BehaviorGrade {
    TOT("Tốt"),
    KHA("Khá"),
    TRUNG_BINH("Trung bình"),
    YEU("Yếu");

    private final String value;

    BehaviorGrade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 