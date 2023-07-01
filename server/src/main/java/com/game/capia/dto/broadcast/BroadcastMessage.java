package com.game.capia.dto.broadcast;

import lombok.Getter;
import lombok.Setter;

/*
정보전달의 단위는 corporate
 */

@Setter
@Getter
public class BroadcastMessage {
    private String type;
    private String subType;
    private String status;
    private Object payload;

}
