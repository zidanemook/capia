package com.game.capia.logic.world;

import com.game.capia.logic.map.CityInfoLogic;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WorldLogic {
    @NonNull
    private String name;
    private List<CityInfoLogic> cityInfoList = new ArrayList<>();

    public void dispose()
    {
        for(CityInfoLogic cityInfoLogic : cityInfoList)
            cityInfoLogic.dispose();
    }
}
