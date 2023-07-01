package com.game.capia.core.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.capia.dto.broadcast.BroadcastMessage;
import com.game.capia.dto.connection.DisconnectDTO;
import com.game.capia.model.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
class SocketNUser{
    private WebSocketSession webSocketSession;
    private User user;

    SocketNUser(WebSocketSession webSocketSession, User user){
        this.webSocketSession = webSocketSession;
        this.user = user;
    }

}

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionManager {

    private final ObjectMapper objectMapper;
    private final Map<String, SocketNUser> sessions = new ConcurrentHashMap<>();
    private final Map<String, SocketNUser> disconnectedSessions = new ConcurrentHashMap<>();
    public void registerSession(String sessionId, WebSocketSession session, User user) {
        sessions.put(sessionId, new SocketNUser(session, user));
    }
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void broadcast(BroadcastMessage broadcastMessage) {
        Iterator<Map.Entry<String, SocketNUser>> iterator = sessions.entrySet().iterator();

        while(iterator.hasNext()){
            Map.Entry<String, SocketNUser> entry = iterator.next();
            String sessionId = entry.getKey();
            SocketNUser socketNUser = entry.getValue();

            String responseJson;
            try {
                responseJson = objectMapper.writeValueAsString(broadcastMessage);
                socketNUser.getWebSocketSession().sendMessage(new TextMessage(responseJson));
            } catch (JsonProcessingException e) {
                log.error("Json processing error", e);
            } catch (IOException e) {
                log.error("Session communication error", e);
                iterator.remove();
                disconnectedSessions.put(sessionId, socketNUser);
            }
        }
    }

    private void handleDisconnectedSessions() {
        for(String sessionId: disconnectedSessions.keySet()) {
            // Remove from disconnected sessions
            disconnectedSessions.remove(sessionId);

            log.info("Session removed due to communication error: " + sessionId);
            // Broadcast disconnection to others
            DisconnectDTO disconnectDTO = DisconnectDTO.builder()
                    //.id()
                    //.username()
                    .build();
            BroadcastMessage disconnectionNotice = new BroadcastMessage();
            disconnectionNotice.setType("BROADCAST");
            disconnectionNotice.setSubType("DISCONNECT");
            disconnectionNotice.setStatus("OK");
            disconnectionNotice.setPayload(disconnectDTO);

            // Send disconnection message directly without using broadcast
            for (SocketNUser socketNUser : sessions.values()) {
                try {
                    String messageJson = objectMapper.writeValueAsString(disconnectionNotice);
                    socketNUser.getWebSocketSession().sendMessage(new TextMessage(messageJson));
                } catch (JsonProcessingException e) {
                    log.error("Json processing error", e);
                } catch (IOException e) {
                    log.error("Session communication error", e);
                }
            }
        }
    }
}
