package com.game.capia.model.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<CityBase, Long> {

    Optional<CityBase> findByName(String name);

    @Query(value = "SELECT * FROM city_tb ORDER BY RAND() LIMIT :cnt", nativeQuery = true)
    List<CityBase> findRandomCities(@Param("cnt") int cnt);

    @Transactional
    @Modifying
    @Query("DELETE FROM CityBase c WHERE c.name = ?1")
    void deleteByName(String name);
}