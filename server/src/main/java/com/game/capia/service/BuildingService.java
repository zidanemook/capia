package com.game.capia.service;

import com.game.capia.dto.building.BuildingRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class BuildingService {
    public void handleRequest(WebSocketSession session, BuildingRequest request) {
        // 건설 요청 처리 로직
    }
}
