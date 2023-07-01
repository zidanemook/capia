package com.capia.websocket;

import com.capia.dto.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;


public class GameWebSocketClient extends WebSocketClient {
    private ObjectMapper objectMapper = new ObjectMapper();

    private Consumer<ResponseMessage> onMessageCallback;

    public void setOnMessageCallback(Consumer<ResponseMessage> onMessageCallback) {
        this.onMessageCallback = onMessageCallback;
    }

    public GameWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // Logic for when the connection is opened
    }

    @Override
    public void onMessage(String message) {
        try {
            // Deserialize the message into a ResponseMessage object
            ResponseMessage response = objectMapper.readValue(message, ResponseMessage.class);

            // Call the callback function
            if (onMessageCallback != null) {
                onMessageCallback.accept(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void onClose(int code, String reason, boolean remote) {
        // Logic for when the connection is closed
    }

    @Override
    public void onError(Exception ex) {
        // Logic for handling errors
        ex.printStackTrace();
    }
}

