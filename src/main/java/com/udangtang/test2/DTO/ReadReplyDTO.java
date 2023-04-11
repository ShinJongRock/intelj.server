package com.udangtang.test2.DTO;

import lombok.Data;

@Data
public class ReadReplyDTO {
    private int roomNum;
    private int meetNum;
    private int replyNum;
    private String replyText;
    private String replyWriter;
}
