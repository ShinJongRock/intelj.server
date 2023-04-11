package com.udangtang.test2.DTO;

import lombok.Data;

import java.util.Date;

@Data
// reply 생성 DTO
public class CreateReplyDTO {
    private int roomNum;
    private int meetNum;
    private String replyText;
    private String replyWriter;
}
