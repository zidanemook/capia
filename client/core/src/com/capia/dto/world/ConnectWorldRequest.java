package com.capia.dto.world;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class ConnectWorldRequest {

    @NonNull
    String worldName;

    @NonNull
    String userName;

    @NonNull
    String password;
}
