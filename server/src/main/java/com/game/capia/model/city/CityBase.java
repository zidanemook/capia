package com.game.capia.model.city;

import lombok.*;

import javax.persistence.*;

//real city data. not allowed to modify
//삭제되지 않는 데이터. 변경되지 않는 데이터

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "city_tb")
@Entity
public class CityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

}
