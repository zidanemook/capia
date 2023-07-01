package com.capia.model.map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoadData {

    private int x;

    private int y;

    private int width;

    private int height;

    // MapName 테이블 참조
    private CityInfo cityInfo;
}
