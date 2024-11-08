package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.AshesOfWarWeapon;

@Repository
public interface AshesOfWarWeaponRepository extends JpaRepository<AshesOfWarWeapon, Long> {
}
