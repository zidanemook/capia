package com.capia.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMessage {
    private String type;

    private String status;
    private String subType;
    private Object payload;
}
