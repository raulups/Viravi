package com.example.tfgviravidam.features.app.models;

public class Message {
    private String sender;
    private String text;
    private String timestamp;

    private Boolean owner;

    public Message() {}

    public Message(String sender, String text, String timestamp, Boolean owner) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
        this.owner = owner;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Message{" +
                "sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
