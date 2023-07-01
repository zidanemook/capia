package com.game.capia.dto.world;

import lombok.*;

@Setter
@Getter
public class ConnectWorldRequest {

    String worldName;

    @NonNull
    String userName;

    @NonNull
    String password;
}
