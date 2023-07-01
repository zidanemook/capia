package com.game.capia.core.dummy;

import com.game.capia.model.city.CityBase;
import com.game.capia.model.city.CityRepository;
import com.game.capia.model.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class DataInit extends DummyEntity{

    @Autowired
    CityRepository cityRepository;

    @Autowired
    private ProductBaseRepository productBaseRepository;

    @Autowired
    private ProductMaterialBaseRepository productMaterialBaseRepository;

    @Profile("dev")
    @Bean
    CommandLineRunner init(){
        return args -> {

            city();
            products();





        };
    }

    public void city(){
        String[] cityNames = {
                "Seoul", "Busan", "Incheon", "Daegu", "Daejeon",
                "Gwangju", "Suwon", "Ulsan", "Changwon", "Seongnam",
                "Tokyo", "Yokohama", "Osaka", "Nagoya", "Sapporo",
                "New York", "Los Angeles", "Chicago", "Houston", "Philadelphia",
                "London", "Manchester", "Birmingham", "Leeds", "Glasgow",
                "Paris", "Marseille", "Lyon", "Toulouse", "Nice",
                "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt",
                "Stuttgart", "Düsseldorf", "Dortmund", "Essen", "Leipzig",
                "Beijing", "Shanghai", "Chongqing", "Tianjin", "Guangzhou",
                "Shenzhen", "Wuhan", "Chengdu", "Hangzhou", "Nanjing",
                "Madrid", "Barcelona", "Valencia", "Seville", "Zaragoza",
                "Rome", "Milan", "Naples", "Turin", "Palermo",
                "Athens", "Thessaloniki", "Patras", "Heraklion", "Larissa",
                "Cairo", "Alexandria", "Giza", "Shubra El Kheima", "Port Said",
                "Helsinki", "Espoo", "Tampere", "Vantaa", "Oulu",
                "Ottawa", "Edmonton", "Calgary", "Mississauga", "Brampton",
                "Warsaw", "Krakow", "Lodz", "Wroclaw", "Poznan",
                "Lisbon", "Porto", "Vila Nova de Gaia", "Amadora", "Braga",
                "Algiers", "Oran", "Constantine", "Annaba", "Blida",
                "Dublin", "Cork", "Limerick", "Galway", "Waterford",
                "Oslo", "Bergen", "Stavanger", "Trondheim", "Fredrikstad",
                "Vienna", "Graz", "Linz", "Salzburg", "Innsbruck",
                "Istanbul", "Ankara", "Izmir", "Bursa", "Adana",
                "Lima", "Arequipa", "Trujillo", "Chiclayo", "Piura",
                "Sofia", "Plovdiv", "Varna", "Burgas", "Ruse",
                "Copenhagen", "Aarhus", "Odense", "Aalborg", "Frederiksberg",
                "Budapest", "Debrecen", "Szeged", "Miskolc", "Pecs",
                "Sydney", "Melbourne", "Brisbane", "Perth", "Adelaide"
        };

        for(String cityName : cityNames) {
            cityRepository.save(newCityBase(cityName));
        }
    }

    public void products(){
        //crops
        ProductBase cocoaPS = productBaseRepository.save(newProductBase("Cocoa", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase coconutPS = productBaseRepository.save(newProductBase("Coconut", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase cornPS = productBaseRepository.save(newProductBase("Corn", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase cottonPS = productBaseRepository.save(newProductBase("Cotton", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase flaxFiberPS = productBaseRepository.save(newProductBase("FlaxFiber", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase grapesPS = productBaseRepository.save(newProductBase("Grapes", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase lemonPS = productBaseRepository.save(newProductBase("Lemon", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase rubberPlantPS = productBaseRepository.save(newProductBase("RubberPlant", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase strawberryPS = productBaseRepository.save(newProductBase("Strawberry", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase sugarCanePS = productBaseRepository.save(newProductBase("SugarCane", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase tobaccoPS = productBaseRepository.save(newProductBase("Tobacco", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase wheatPS = productBaseRepository.save(newProductBase("Wheat", ProductCategory.RawMaterial, 0, true, 0, 0));

        //livestock
        ProductBase beefPS = productBaseRepository.save(newProductBase("Beef", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase milkPS = productBaseRepository.save(newProductBase("Milk", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase leatherPS = productBaseRepository.save(newProductBase("Leather", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase chickenPS = productBaseRepository.save(newProductBase("Chicken", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase eggsPS = productBaseRepository.save(newProductBase("Eggs", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase porkPS = productBaseRepository.save(newProductBase("Pork", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase lambPS = productBaseRepository.save(newProductBase("Lamb", ProductCategory.RawMaterial, 0, true, 0, 0));
        ProductBase woolPS = productBaseRepository.save(newProductBase("Wool", ProductCategory.RawMaterial, 0, true, 0, 0));


        ProductBase oilPS = productBaseRepository.save(newProductBase("Oil", ProductCategory.RawMaterial, 0, true, 0, 0));

        //semi products
        ProductBase dyestuffPS = productBaseRepository.save(newProductBase("Dyestuff", ProductCategory.SemiProducts, 0, true, 50, 50));
        ProductBase linenPS = productBaseRepository.save(newProductBase("Linen", ProductCategory.SemiProducts, 0, true, 50, 50));
        ProductBase textilesPS = productBaseRepository.save(newProductBase("Textiles", ProductCategory.SemiProducts, 0, true, 50, 50));

        ProductBase bedPS = productBaseRepository.save(newProductBase("Bed", ProductCategory.Furniture, 40, true, 50, 50));
        ProductBase timberPS = productBaseRepository.save(newProductBase("Timber", ProductCategory.RawMaterial, 0, true, 0, 0));

        //products
        ProductBase blazerPS = productBaseRepository.save(newProductBase("Blazer", ProductCategory.Apparel, 0, true, 50, 50));

        //material <-> products
        productMaterialBaseRepository.save(newProductMaterialBase(bedPS, timberPS));
        productMaterialBaseRepository.save(newProductMaterialBase(dyestuffPS, oilPS));
        productMaterialBaseRepository.save(newProductMaterialBase(dyestuffPS, timberPS));
        productMaterialBaseRepository.save(newProductMaterialBase(linenPS, flaxFiberPS));
        productMaterialBaseRepository.save(newProductMaterialBase(textilesPS, cottonPS));
    }
}
