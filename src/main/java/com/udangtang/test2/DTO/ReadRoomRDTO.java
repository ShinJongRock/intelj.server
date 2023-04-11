package com.udangtang.test2.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
// 리스폰스 관련 DTO
public class ReadRoomRDTO {
    private int roomNum;
    private String roomName;
    private String roomHost;
    private int roomMember;

}
