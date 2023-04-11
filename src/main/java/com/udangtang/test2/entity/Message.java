package com.udangtang.test2.entity;

import lombok.Data;

@Data
public class Message {

    private String room;

    public Message() {
    }

    public Message(String room) {
        this.room = room;
    }

}
