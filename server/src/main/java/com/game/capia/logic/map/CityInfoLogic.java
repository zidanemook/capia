package com.game.capia.logic.map;

import com.game.capia.core.map.TileMap;
import com.game.capia.logic.world.WorldLogic;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CityInfoLogic {

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

    private WorldLogic worldLogic;

    //@Builder.Default
    //private List<RoadData> roadDataList = new ArrayList<>();

    //private TileMap tileMap;

    public void dispose()
    {

        //tileMap.dispose();
    }
}
