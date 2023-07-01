package com.game.capia.dto.connection;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class DisconnectDTO {
    private Long id;
    private String username;
}
