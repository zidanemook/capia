package com.game.capia.model.product;

import com.game.capia.model.firm.Firm;

import javax.persistence.*;

@Entity
@Table(name = "product_tb")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Firm_id")
    private Firm firm;

    private Long quantity;

    private String name;

    private String category;

    //농작물 생산원가 = (12개월 * 월급지출) / 생산량 lbs
    //포도 생산원가 4.53 = (12 *  1078905) / 2852200lbs

    private Long price;

    private Integer quality;

    private Integer materialQuality;

    private Integer productionQuality;

    private Integer brand;

    private Integer necessityLevel;

    private Boolean invented;

    private Long purchaseCost;

    private Long freightCost;

}
