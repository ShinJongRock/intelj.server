package com.udangtang.test2.entity;

import lombok.Data;

@Data
public class Ice {
    private String ice;
    private String room;

    public Ice() {
    }

    public Ice(String room, String ice) {
        this.room = room;
        this.ice = ice;
    }
}
