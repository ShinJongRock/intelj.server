package com.udangtang.test2.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class ReadMeetingDTO {
    private int roomNum;
    private int meetNum;
    private String meetTitle;
    private String meetDate;
}
