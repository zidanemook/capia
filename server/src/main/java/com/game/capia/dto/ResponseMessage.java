package com.game.capia.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMessage {
    private String type;
    private String subType;
    private String status;

    private Object payload;  // com.fasterxml.jackson.databind.JsonNode

    // getters and setters
}
