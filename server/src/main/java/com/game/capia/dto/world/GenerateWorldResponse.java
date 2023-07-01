package com.game.capia.dto.world;

import com.game.capia.dto.map.CityInfoDTO;
import com.game.capia.model.map.CityInfo;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateWorldResponse {

    String name;
    int width;
    int height;
    List<CityInfoDTO> cities;

}
