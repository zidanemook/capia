package com.game.capia.model.product;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "productbase_tb")
public class
ProductBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private ProductCategory category;
    //set in farm, factory
    //private Long price;
    private Integer necessityLevel;
    private Boolean invented;
    //The percentage of influence of technology on quality.
    private Integer productionQuality;

    //If the category is raw material, this variable randomly represents the quality of the material on a scale of 0 to 100
    //Otherwise, it represents the percentage of influence of raw material on quality
    private Integer rawMaterialQuality;

    @OneToMany(mappedBy = "product")
    private List<ProductMaterialBase> materials = new ArrayList<>();

}
