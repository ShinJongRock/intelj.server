package com.udangtang.test2.controller;

import com.udangtang.test2.DAO.UdangDAO;
import com.udangtang.test2.DTO.*;

import com.udangtang.test2.service.SttService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


@RestController
public class UdangController {
    @Autowired
    private UdangDAO udangDAO;

    @RequestMapping("yTest")
    public String running(@RequestPart(value="file") MultipartFile file) throws IOException, EncoderException {
        SttService sttService = new SttService();
        ArrayList<String> result = new ArrayList<>();
        String token = "";
        String resultId = "";
        String contentsText = "";
        RecordDTO recordDTO = new RecordDTO();


        System.out.println(file);

        System.out.println("------------------");
//        // 파일 path, 확장자명 설정
        String tempPath ="C:\\fileTest" + "\\" + file.getOriginalFilename();

//        // 해당 경로에 파일 생성
        File pcmFile = new File(tempPath + ".pcm");

//        // fileStream에 MultipartFile file의 데이터를 입력
        InputStream fileinputStream = file.getInputStream();
//        // fileStream에 담긴 데이터를 convfile변수에 복사
        FileUtils.copyInputStreamToFile(fileinputStream, pcmFile);

        System.out.println("--------< 프론트에서 넘어온 파일 변환 결과(pcmFile)...>----------");
        System.out.println(pcmFile);

        try {
            System.out.println("--------<< STT 진행 중...>>----------");
            token = sttService.test();                         // 토큰 만들기

            resultId = sttService.run(token, pcmFile);         // 녹음 파일 ID 생성하기
            System.out.println("--------< STT 후, ID...>----------");
            System.out.println("resultID : " + resultId);

            result = sttService.get(token, resultId);         // 녹음 파일 ID로 AI로 텍스트 json 형태로 저장하기
            System.out.println("--------< STT 후, 결과값...>----------");
            System.out.println(result);


            for(String sResult : result){
                contentsText += sResult + "\n";
            }


            // STT 결과값 dto의 contentsText에 세팅
            recordDTO.setContentsText(contentsText);
            recordDTO.setRoomNum(1);
            recordDTO.setMeetNum(2);
            recordDTO.setContentsTime("23:30:00");
            recordDTO.setContentsWriter("갸갸갹");

            // DAO로 DB연동(함수명 변경할것)
            udangDAO.createTest(recordDTO);


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("음성 파일 변환에 문제가 생겼습니다.");
        }

        return contentsText;
    }



    //회원가입 및 로그인 더 해야됨
    @PostMapping("login")
    public boolean login(@RequestBody UpdateReplyDTO.UserDTO userDTO){
        try{
            udangDAO.login(userDTO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }


    //방 생성   c
    @PostMapping("/createRoom")
    public boolean createRoom(@RequestBody CreateRoomDTO createRoomDTO){
        try{
            udangDAO.InsertRoom(createRoomDTO);
            return true;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //방 입장
    @PostMapping("/joinRoom")
    public boolean joinRoom(@RequestBody JoinRoomDTO joinRoomDTO){
        try{
            udangDAO.joinRoom(joinRoomDTO);
            udangDAO.updateMember(joinRoomDTO);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

//    //방 목록 가져오기  r
    @GetMapping("/readRoom")
    public List readRoom(@RequestBody ReadRoomDTO readRoomDTO) {
        ArrayList<ReadRoomRDTO> list = new ArrayList<>();
        try{
            list = udangDAO.readRoom(readRoomDTO);
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


//    // 방 목록 삭제   d
//    @PostMapping("/deleteRoom")
//    public boolean deleteRoom(){
//
//    }


    // ----------------------- <meeting> -----------------------
    // meeting 생성  c
    @PostMapping("/createMeeting")
    public Boolean createMeeting(@RequestBody CreateMeetingDTO createMeetingDTO) {
        try{
            udangDAO.createMeeting(createMeetingDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    // meeting 가져오기 r
    @PostMapping ("/readMeeting")
    public ArrayList<ReadMeetingDTO> readMeeting(@RequestBody ReadMeetingDTO readMeetingDTO) {
        ArrayList<ReadMeetingDTO> readMeetingList = new ArrayList<>();
        try{
            readMeetingList = udangDAO.readMeeting(readMeetingDTO);
            return readMeetingList;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // meeting 삭제  d
    @PostMapping("/deleteMeeting")
    public boolean deleteMeeting(@RequestBody DeleteMeetingDTO deleteMeetingDTO) {
        boolean check = false;

        try{
            // meeting 삭제 전, 해당 meeting의 reply 전체 삭제
            udangDAO.deleteFromMeetingReply(deleteMeetingDTO);
            //  삭제 후, 해당 meeting 삭제
            udangDAO.deleteMeeting(deleteMeetingDTO);
            check = true;
        }catch (Exception e){
            e.printStackTrace();
            return check;
        }

        return check;
    }


    // ----------------------- <회의내용> -----------------------
    // 회의내용 작성 c
    @PostMapping("/createContent")
    public void createContent() {

    }

    // 회의내용 가져오기 r
    @PostMapping("/readContent")
    public void readContent() {

    }

    // 회의내용 수정 u
    @PostMapping("/updateContent")
    public void updateContent() {

    }

    // 회의내용 삭제 d
    @PostMapping("/deleteContent")
    public void deleteContent() {

    }


    // ----------------------- <reply> -----------------------
    // reply 추가 c
    @PostMapping("/createReply")
    public boolean createReply(@RequestBody CreateReplyDTO createReplyDTO) {
        try {
            udangDAO.createReply(createReplyDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // reply 리스트 가져오기 r
    @PostMapping("/readReply")
    public ArrayList<ReadReplyDTO> readReply(@RequestBody ReadReplyDTO readReplyDTO) {
        ArrayList<ReadReplyDTO> list;
        try {
            list = udangDAO.readReply(readReplyDTO);
            return list;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // reply 수정 u
    @PostMapping("/updateReply")
    public boolean updateReply(@RequestBody UpdateReplyDTO updateReplyDTO) {
        try {
            udangDAO.updateReply(updateReplyDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // reply 삭제 d
    @PostMapping("/deleteReply")
    public boolean deleteReply(@RequestBody DeleteReplyDTO deleteReplyDTO) {
        try {
            udangDAO.deleteReply(deleteReplyDTO);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


}

