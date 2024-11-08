package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Weapon;

import java.util.List;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Long> {
    @Query("SELECT w FROM Build b JOIN b.buildWeapons bw JOIN bw.ashesOfWarWeapon aow JOIN aow.weapon w WHERE b.id = :id")
    List<Weapon> findWeaponsByBuildId(@Param("id") Long id);
}