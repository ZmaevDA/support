package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.BuildWeapon;

@Repository
public interface BuildWeaponRepository extends JpaRepository<BuildWeapon, Long> {
    void deleteAllByBuildId(Long buildId);
}
