package com.game.capia.model.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CityInfoRepository extends JpaRepository<CityInfo, Long> {

    Optional<CityInfo> findByName(String name);


    List<CityInfo> findAllById(Long id);

    @Query("SELECT c FROM CityInfo c WHERE c.world.id = :worldId")
    List<CityInfo> findAllByWorldId(@Param("worldId") Long worldId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CityInfo m WHERE m.name = ?1")
    void deleteByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM CityInfo c WHERE c.world.id = :worldId")
    void deleteByWorldId(@Param("worldId") Long worldId);
}