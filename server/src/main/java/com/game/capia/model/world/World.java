package com.game.capia.model.world;

import com.game.capia.model.map.CityInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "world_tb")
@Entity
public class World {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int heigth;

    @OneToMany(mappedBy = "world", cascade = CascadeType.ALL)
    private List<CityInfo> CityInfoList = new ArrayList<>();
}
