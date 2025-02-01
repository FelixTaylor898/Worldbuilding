package com.java.backend.data;

import com.java.backend.model.Chara;
import com.java.backend.model.Species;
import com.java.backend.model.World;
import com.java.backend.model.Location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CharaRepository extends JpaRepository<Chara, Integer> {
    List<Chara> findByWorld(World world);
    List<Chara> findBySpecies(Species species);
    List<Chara> findByLocation(Location location);
    @Query("SELECT c FROM Chara c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Chara> findByName(@Param("name") String name);


}
