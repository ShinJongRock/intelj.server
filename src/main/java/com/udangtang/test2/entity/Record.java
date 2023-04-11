package com.udangtang.test2.entity;

import lombok.Data;

@Data
public class Record {
    private String room;
    private String contentsText;

    public Record() {

    }

    public Record(String room, String contentsText) {
        this.room = room;
        this.contentsText = contentsText;
    }
}
