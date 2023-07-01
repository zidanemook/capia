package com.game.capia.dto.world;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class DeleteWorldRequest {

    @NonNull
    String name;
}
