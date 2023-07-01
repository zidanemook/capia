package com.capia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessage {
    private String type;
    private String subType;
    private Object payload;
}
