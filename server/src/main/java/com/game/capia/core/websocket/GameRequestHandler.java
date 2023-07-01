package com.game.capia.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.capia.dto.RequestMessage;
import com.game.capia.dto.building.BuildingRequest;
import com.game.capia.dto.user.UserRequest;
import com.game.capia.service.BuildingService;
import com.game.capia.service.WorldService;
import com.game.capia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class GameRequestHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final BuildingService buildingService;

    private final UserService userService;

    private final WorldService worldService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            RequestMessage parsedRequestMessage = objectMapper.readValue(message.getPayload(), RequestMessage.class);
            if ("BUILDING".equalsIgnoreCase(parsedRequestMessage.getType())) {
                BuildingRequest buildingRequest = objectMapper.treeToValue((TreeNode) parsedRequestMessage.getPayload(), BuildingRequest.class);
                buildingService.handleRequest(session, buildingRequest);
            }
            else if("USER".equalsIgnoreCase(parsedRequestMessage.getType())){
                UserRequest userRequest = objectMapper.treeToValue((TreeNode) parsedRequestMessage.getPayload(), UserRequest.class);
                userService.handleRequest(session, userRequest);
            }
            else if("WORLD".equalsIgnoreCase(parsedRequestMessage.getType())){
                worldService.handleRequest(session, parsedRequestMessage);
            }
        } catch (JsonProcessingException e) {
            Logger.getLogger(GameRequestHandler.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // 클라이언트와의 WebSocket 연결이 성립될 때마다 이 메소드가 호출됩니다.
        System.out.println("Connection established");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 클라이언트와의 WebSocket 연결이 닫힐 때마다 이 메소드가 호출됩니다.
        System.out.println("Connection closed");
    }
}