package com.udangtang.test2.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UpdateReplyDTO {
    private int roomNum;
    private int meetNum;
    private int replyNum;
    private String replyText;
    private String replyWriter;

    @Data
    @Getter
    @Setter
    public static class UserDTO {

        // user ( 회원 목록 )
        private String id;
        private String name;
    }
}
