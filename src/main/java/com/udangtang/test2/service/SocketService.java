package com.udangtang.test2.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.udangtang.test2.entity.Record;
import com.udangtang.test2.entity.Answer;
import com.udangtang.test2.entity.Ice;
import com.udangtang.test2.entity.Message;
import com.udangtang.test2.entity.Offer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SocketService {

    // room : 메시지를 받을 방, eventName : 메시지를 받을 이벤트,
    // SocketIOClient senderClient : 메시지를 보내고 싶은 사람(방에 메시지를 보내는 동안은 제외)
    // message : 메시지
    public void sendMessageRoom(String room, String eventName, SocketIOClient senderClient, String roomData) {
        // 이 for 반복문을 통해 주어진 방의 모든 사용자를 얻는다.
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Message(roomData));
            }
        }
    }

    public void sendMessageOffer(String room, String eventName, SocketIOClient senderClient, String offer) {
        // 이 for 반복문을 통해 주어진 방의 모든 사용자를 얻는다.
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Offer(room, offer));
            }
        }
    }

    public void sendMessageAnswer(String room, String eventName, SocketIOClient senderClient, String answer) {
        // 이 for 반복문을 통해 주어진 방의 모든 사용자를 얻는다.
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Answer(room, answer));
            }
        }
    }

    public void sendMessageIce(String room, String eventName, SocketIOClient senderClient, String ice) {
        // 이 for 반복문을 통해 주어진 방의 모든 사용자를 얻는다.
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
            if (!client.getSessionId().equals(senderClient.getSessionId())) {
                client.sendEvent(eventName,
                        new Ice(room, ice));
            }
        }
    }

    public void sendMessageRecord(String room, String eventName, SocketIOClient senderClient, String recordResult) {
        // 이 for 반복문을 통해 주어진 방의 모든 사용자를 얻는다.
        for (
                SocketIOClient client : senderClient.getNamespace().getRoomOperations(room).getClients()) {
                // 모든 클라이언트에게 이 메세지(data)를 보낸다.
                client.sendEvent(eventName, new Record(room, recordResult));
        }
    }

}