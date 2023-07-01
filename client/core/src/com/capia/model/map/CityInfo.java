package com.capia.model.map;

import com.capia.model.world.World;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CityInfo {

    private String name;

    private int width;

    private int height;

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

    private World world;

    @Builder.Default
    private List<RoadData> roadDataList = new ArrayList<>();

    public void dispose()
    {
    }

}
