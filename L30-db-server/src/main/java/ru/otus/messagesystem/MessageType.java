package ru.otus.messagesystem;

public enum MessageType {
    ALL_USERS("allUsers"),
    INSERT_USER("insertUser");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
