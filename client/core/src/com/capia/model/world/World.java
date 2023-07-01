package com.capia.model.world;

import com.capia.game.Capia;
import com.capia.model.map.CityInfo;
import com.capia.model.map.TileMap;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class World {
    @NonNull
    private String name;

    private int width;
    private int height;

    private List<CityInfo> cityInfoList = new ArrayList<>();

    private TileMap tileMap;

    public void render(Capia game, int selectCity) {

        if( 0 <= selectCity && selectCity < cityInfoList.size())
            tileMap.render(game);
    }

    public void dispose()
    {
        for(CityInfo cityInfo : cityInfoList)
            cityInfo.dispose();

        tileMap.dispose();
    }

    public void generateWorld()
    {

    }
}
