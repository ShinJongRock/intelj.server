package com.udangtang.test2.entity;

import lombok.Data;

@Data
public class Offer {

    private String offer;
    private String room;

    public Offer() {
    }

    public Offer(String room, String offer) {
        this.room = room;
        this.offer = offer;
    }
}
