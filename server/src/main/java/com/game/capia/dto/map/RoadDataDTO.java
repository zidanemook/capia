package com.game.capia.dto.map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoadDataDTO {

    int x;
    int y;

    int width;
    int height;


}
