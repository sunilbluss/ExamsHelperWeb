package com.grudus.helpers;


public enum Change {
    CREATE("SUBJECT_NEW"),
    UPDATE("SUBJECT_EDIT"),
    DELETE("SUBJECT_REMOVED");

    private final String value;

    Change(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
