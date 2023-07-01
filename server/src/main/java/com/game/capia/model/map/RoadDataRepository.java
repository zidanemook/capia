package com.game.capia.model.map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RoadDataRepository extends JpaRepository<RoadData, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM RoadData m WHERE m.cityInfo.id = ?1")
    void deleteByMapNameId(Long mapNameId);
    List<RoadData> findByCityInfo(CityInfo cityInfo);
}