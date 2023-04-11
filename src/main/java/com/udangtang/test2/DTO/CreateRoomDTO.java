package com.udangtang.test2.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Getter
@Setter
public class CreateRoomDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomNum;
    private String roomName;
    private String roomKey;
    private String roomPw;
    private String roomHost;
    private int roomMember=1;
}
