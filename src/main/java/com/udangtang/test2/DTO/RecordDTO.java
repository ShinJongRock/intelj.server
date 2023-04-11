package com.udangtang.test2.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class RecordDTO {
    private int roomNum;
    private int meetNum;
    private String contentsText;
    private String contentsTime;
    private String contentsWriter;

    public RecordDTO() {

    }
    public RecordDTO(int roomNum, int meetNum, String contentsText, String contentsTime, String contentsWriter){
        this.roomNum = roomNum;
        this.meetNum = meetNum;
        this.contentsText = contentsText;
        this.contentsTime = contentsTime;
        this.contentsWriter = contentsWriter;
    }
}
