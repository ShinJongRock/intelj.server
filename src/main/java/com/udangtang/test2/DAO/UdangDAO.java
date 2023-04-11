package com.udangtang.test2.DAO;

import com.udangtang.test2.DTO.*;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UdangDAO {

    // 로그인(회원가입) 정보 저장
    // insert 구문 : 데이터가 없으면 isert 하고 있으면 아무것도 안하는 query문
    @Insert("insert into testDB.user( id , name ) " +
            "SELECT #{id} , #{name} from dual where not exists(select * from testDB.user where id =#{id} and name = #{name}")
    void login(UpdateReplyDTO.UserDTO dto);

    // 로그인 정보 삭제
    @Delete("DELETE FROM testDB.user WHERE id = #{userId}")
    void DeleteUser(UpdateReplyDTO.UserDTO dto);


    // ----------------------- <room> -----------------------
    // room 생성
    @Insert("INSERT INTO testDB.room (roomName , roomKey , roomPw , roomHost , roomMember ) VALUES (#{roomName},#{roomKey},#{roomPw},#{roomHost},#{roomMember})")
    void InsertRoom(CreateRoomDTO dto);


    // room 참여
    @Insert("insert into testDB.list ( roomNum , id ) values ((select roomNum from room where roomKey = {#roomKey} and roomPw =#{roomPw}) , #{id} )")
    boolean joinRoom(JoinRoomDTO dto);
    // room 인원 수정
    @Update("update room r set r.roomMember = r.roomMember+1 where roomKey = #{roomKey}")
    boolean updateMember(JoinRoomDTO dto);


    // room 가져오기 ReadRoom
    @Select("select roomNum , roomName , roomHost , roomMember from room where roomNum in ( select roomNum from list where id = #{id})")
    ArrayList<ReadRoomRDTO> readRoom(ReadRoomDTO dto);


    // ----------------------- <meeting> -----------------------
    // meeting 생성
    @Insert("INSERT INTO testDB.meeting (roomNum, meetTitle, meetDate) VALUES (#{roomNum}, #{meetTitle}, #{meetDate})")
    void createMeeting(CreateMeetingDTO createMeetingDTO);

    // meeting 가져오기
    // 들어온 roomNum와 같은 방의 정보들 SELECT
    @Select("SELECT * FROM testDB.meeting WHERE roomNum = #{roomNum}")
    ArrayList<ReadMeetingDTO> readMeeting(ReadMeetingDTO readMeetingDTO);

    // meeting 삭제(해당 meeting의 meetNum을 갖고 있는 댓글들 먼저 삭제하게 만들 것)
    @Delete("DELETE FROM testDB.meeting WHERE roomNum = #{roomNum} AND meetNum = #{meetNum}")
    boolean deleteMeeting(DeleteMeetingDTO deleteMeetingDTO);
    @Delete("DELETE FROM testDB.reply WHERE roomNum = #{roomNum} AND meetNum = #{meetNum}")
    void deleteFromMeetingReply(DeleteMeetingDTO deleteMeetingDTO);



    // ----------------------- <reply> -----------------------
    // reply 추가
    @Insert("INSERT INTO testDB.reply (roomNum, meetNum, replyText, replyWriter) VALUES (${roomNum}, ${meetNum}, '${replyText}', '${replyWriter}')")
    void createReply(CreateReplyDTO createReplyDTO);

    // reply 리스트 가져오기
    @Select("SELECT * FROM testDB.reply WHERE roomNum = ${roomNum} AND meetNum = ${meetNum}")
    ArrayList<ReadReplyDTO> readReply(ReadReplyDTO readReplyDTO);

    // reply 수정
    @Update("UPDATE testDB.reply SET replyText = '${replyText}' WHERE roomNum = ${roomNum} AND meetNum = ${meetNum} AND replyWriter = '${replyWriter}'")
    void updateReply(UpdateReplyDTO updateReplyDTO);

    // reply 삭제
    @Delete("DELETE FROM testDB.reply WHERE roomNum = #{roomNum} AND meetNum = #{meetNum} AND replyWriter = '${replyWriter}')")
    void deleteReply(DeleteReplyDTO deleteReplyDTO);

    // --------------------
    // 테스트
    @Insert("INSERT INTO testDB.contents (roomNum, meetNum, contentsText, contentsTime, contentsWriter) VALUES (${roomNum}, ${meetNum}, '${contentsText}', '${contentsTime}', '${contentsWriter}')")
    void createTest(RecordDTO recordDTO);

}
