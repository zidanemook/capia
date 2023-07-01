package com.game.capia.model.map;

import com.game.capia.model.world.World;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//변경이 가능한 데이터
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "cityinfo_tb")
public class CityInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private int y;

    @Column(name = "radius", nullable = false)
    private int radius;

    @Column(name = "population", nullable = false)
    private int population;

    @Column(name = "spendingLevel", nullable = false)
    private int spendingLevel;

    @Column(name = "salaryLevel", nullable = false)
    private int salaryLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "economicClimate", nullable = false)
    private EconomicClimate economicClimate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "world_id", nullable = false)
    private World world;

    @OneToMany(mappedBy = "cityInfo", cascade = CascadeType.ALL)
    private List<RoadData> roadDataList = new ArrayList<>();

}
