package com.game.capia.model.firm;

import com.game.capia.model.corporate.Corporate;
import com.game.capia.model.map.CityInfo;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "firm_tb")
public class Firm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="Corporate_id")
    private Corporate corporate;

    @ManyToOne
    @JoinColumn(name="CityInfo_id")
    private CityInfo cityInfo;

    private Integer x;
    private Integer y;
    private Integer width;
    private Integer height;
    private String type;
    private Integer employee;

    @Column(name = "inventoryAsset")
    private Long inventoryAsset;

    @Column(name = "businessAsset")
    private Long businessAsset;

    @Column(name = "landAsset")
    private Long landAsset;

    @Column(name = "annualRevenue")
    private Long annualRevenue;

    @Column(name = "annualProfit")
    private Long annualProfit;

    @Column(name = "salesRevenue")
    private Long salesRevenue;

    @Column(name = "operatingRevenue")
    private Long operatingRevenue;

    @Column(name = "OperatingExpenses")
    private Long operatingExpenses;

    @Column(name = "purchaseCost")
    private Long purchaseCost;

    @Column(name = "freightCost")
    private Long freightCost;

    @Column(name = "costOfSales")
    private Long costOfSales;

    @Column(name = "SalariesExpense")
    private Long salariesExpense;

    @Column(name = "operatingOverhead")
    private Long operatingOverhead;

    private Long advertising;
    private Long training;
    private Long writeOffs;

    @Column(name = "increaseAssetValue")
    private Long increaseAssetValue;
}