package com.capia.dto.world;

import com.capia.dto.map.CityInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
