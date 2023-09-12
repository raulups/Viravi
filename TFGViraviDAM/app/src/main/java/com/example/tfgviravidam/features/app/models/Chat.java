package com.example.tfgviravidam.features.app.models;

import java.io.Serializable;
import java.util.List;

public class Chat implements Serializable {

    private String id;
    private String name,foto;
    private String nameEvent;

    private List<String> users;
    private List<Message> messages;

    public Chat() {}

    public Chat(String id, String name, String foto, String nameEvent, List<String> users, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.foto = foto;
        this.nameEvent = nameEvent;
        this.users = users;
        this.messages = messages;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



    public String getNameEvent() {
        return nameEvent;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", foto='" + foto + '\'' +
                ", nameEvent='" + nameEvent + '\'' +
                ", users=" + users +
                ", messages=" + messages +
                '}';
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}

