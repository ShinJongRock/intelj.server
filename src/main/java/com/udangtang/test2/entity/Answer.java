package com.udangtang.test2.entity;

import lombok.Data;

@Data
public class Answer {
    private String answer;
    private String room;

    public Answer() {
    }

    public Answer(String room, String answer) {
        this.room = room;
        this.answer = answer;
    }
}
