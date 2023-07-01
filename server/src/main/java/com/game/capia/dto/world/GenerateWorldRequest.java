package com.game.capia.dto.world;

import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateWorldRequest {
    @NonNull
    String name;

    private int width;

    private int height;
}
