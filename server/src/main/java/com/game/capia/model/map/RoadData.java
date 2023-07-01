package com.game.capia.model.map;

import lombok.*;
import javax.persistence.*;

//쿼드트리의 leaf 노드를  DB에 저장하기위한 엔티티
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "roaddata_tb", indexes = {@Index(name = "index_cityinfo", columnList = "cityinfo_id")})
@Entity
public class RoadData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x", nullable = false)
    private int x;

    @Column(name = "y", nullable = false)
    private int y;

    @Column(name = "width", nullable = false)
    private int width;

    @Column(name = "height", nullable = false)
    private int height;

    // MapName 테이블 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cityinfo_id", nullable = false)
    private CityInfo cityInfo;
}
