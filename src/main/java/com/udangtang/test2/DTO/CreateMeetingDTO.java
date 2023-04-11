package com.udangtang.test2.DTO;

import lombok.Data;

import java.util.Date;

@Data
// 회의방 생성 DTO
public class CreateMeetingDTO {
    private int roomNum;
    private int meetNum;
    private String meetTitle;
    private Date meetDate;
}
