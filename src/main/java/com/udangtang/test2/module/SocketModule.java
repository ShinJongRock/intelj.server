package com.udangtang.test2.module;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.udangtang.test2.DAO.UdangDAO;
//import com.udangtang.test2.DTO.RecordDTO;
import com.udangtang.test2.PcmToWav;
import com.udangtang.test2.entity.Record;
import com.udangtang.test2.entity.*;
import com.udangtang.test2.service.SocketService;
import com.udangtang.test2.service.SttService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;
    private final SocketService socketService;

    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;

        // 누군가 소켓에 연결할 때 트리거
        server.addConnectListener(onConnected());
        // 누군가 소켓에서 연결을 끊을 때 트리거
        server.addDisconnectListener(onDisconnected());
        // 해당 socket.on(“send_message”)입니다.
        // 주어진 이벤트 이름과 객체 클래스로 이벤트를 처리할 수 있습니다.
        server.addEventListener("join_room", Message.class, onChatReceived());

        server.addEventListener("offer", Offer.class, onOfferReceived());

        server.addEventListener("answer", Answer.class, onAnswerReceived());

        server.addEventListener("ice", Ice.class, onIceReceived());

        // record 라는 키워들 받았다면, 메세지를 보낸다.
        server.addEventListener("message", Record.class, onStt());
    }


    // record 라는 키워드를 받았다면,
    private DataListener<Record> onStt(){
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessageRecord(data.getRoom(),"responseRecord", senderClient, data.getContentsText());
        };
    }


    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessageRoom(data.getRoom(),"welcome", senderClient, data.getRoom());
        };
    }

    private DataListener<Offer> onOfferReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessageOffer(data.getRoom(),"offer", senderClient, data.getOffer());
        };
    }

    private DataListener<Answer> onAnswerReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessageAnswer(data.getRoom(),"answer", senderClient, data.getAnswer());
        };
    }

    private DataListener<Ice> onIceReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.sendMessageIce(data.getRoom(),"ice", senderClient, data.getIce());
        };
    }


    private ConnectListener onConnected() {
        return (client) -> {
            String room = client.getHandshakeData().getSingleUrlParam("room");
            client.joinRoom(room);
            log.info("Socket ID[{}]  Connected to socket", client.getSessionId().toString());
        };
    }

    private DisconnectListener onDisconnected() {
        return client -> {
            log.info("Client[{}] - Disconnected from socket", client.getSessionId().toString());
        };
    }

}