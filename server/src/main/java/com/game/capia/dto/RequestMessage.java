package com.game.capia.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestMessage {
    private String type;

    private String subType;
    private Object payload;

}
