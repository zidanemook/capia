package com.game.capia.dto.world;

import com.game.capia.dto.map.CityInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConnectWorldResponse {

    String name;

    int width;

    int height;
    List<CityInfoDTO> cities;
}
