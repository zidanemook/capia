package com.game.capia.core.dummy;

import com.game.capia.model.city.CityBase;
import com.game.capia.model.product.ProductMaterialBase;
import com.game.capia.model.product.ProductBase;
import com.game.capia.model.product.ProductCategory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.game.capia.model.user.User;

import java.time.LocalDateTime;

public class DummyEntity {
    public User newUser(String username, String fullName){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return User.builder()
                .username(username)
                .password(passwordEncoder.encode("1234"))
                .build();
    }

    public CityBase newCityBase(String name) {
        return CityBase.builder()
                .name(name)
                .build();
    }

    public ProductBase newProductBase(String name, ProductCategory category, Integer necessityLevel, Boolean invented, Integer productionQuality, Integer rawMaterialQuality){
        return ProductBase.builder()
                .name(name)
                .category(category)
                .necessityLevel(necessityLevel)
                .invented(invented)
                .productionQuality(productionQuality)
                .rawMaterialQuality(rawMaterialQuality)
                .build();
    }

    public ProductMaterialBase newProductMaterialBase(ProductBase product, ProductBase material){
        return ProductMaterialBase.builder()
                .product(product)
                .material(material)
                .build();
    }

}
