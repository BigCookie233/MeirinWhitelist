package io.github.bigcookie233.meirin.entity;

public class QueryResult {
    private boolean allowed;
    private MessageType messageType;
    private String message;

    public QueryResult(boolean allowed, MessageType messageType, String message) {
        this.allowed = allowed;
        this.messageType = messageType;
        this.message = message;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
