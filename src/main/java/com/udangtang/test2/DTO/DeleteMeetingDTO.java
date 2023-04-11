package com.udangtang.test2.DTO;

import lombok.Data;

@Data
public class DeleteMeetingDTO {
    // roomNum 과 meetNum 값으로 삭제
    private int roomNum;
    private int meetNum;
}
