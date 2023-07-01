package com.game.capia.model.product;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "product_material")
public class ProductMaterialBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //material 이 모여서 product가 된다. 이때는 ProductMaterialBase 가 material
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;

    //한 개의 Material (재료)이 여러 개의 ProductBase (제품)에 사용될 수 있다. 이때는 ProductMaterialBase 가 product
    @ManyToOne
    @JoinColumn(name = "material_id")
    private ProductBase material;

    private Integer quantity; // 해당 재료가 제품에 필요한 양
}
