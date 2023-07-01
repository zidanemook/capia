package com.capia.dto.world;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class GenerateWorldRequest {
    @NonNull
    String name;

    private int width;

    private int height;
}
