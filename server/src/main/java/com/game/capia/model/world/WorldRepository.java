package com.game.capia.model.world;

import com.game.capia.model.map.CityInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WorldRepository extends JpaRepository<World, Long> {
    @Query("SELECT w.name FROM World w")
    List<String> getAllName();

    Optional<World> findByName(String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM World w WHERE w.name = ?1")
    void deleteByName(String name);

}