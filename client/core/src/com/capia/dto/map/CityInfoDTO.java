package com.capia.dto.map;

import com.capia.model.map.EconomicClimate;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class CityInfoDTO {
    private String name;

    //Longtitude
    private int x;

    //Latitude
    private int y;

    //city radius
    private int radius;

    private int population;

    private int spendingLevel;

    private int salaryLevel;

    private EconomicClimate economicClimate;

    private List<RoadDataDTO> roadDataList;
}
