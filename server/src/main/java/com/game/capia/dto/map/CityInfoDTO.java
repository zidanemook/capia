package com.game.capia.dto.map;

import com.game.capia.model.map.EconomicClimate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class CityInfoDTO {
    private String name;

    //Longtitude
    private float x;

    //Latitude
    private float y;

    //city radius
    private int radius;

    private int population;

    private int spendingLevel;

    private int salaryLevel;

    private EconomicClimate economicClimate;

    private List<RoadDataDTO> roadDataList;

}
