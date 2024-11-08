package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.AshesOfWar;

@Repository
public interface AshesOfWarRepository extends JpaRepository<AshesOfWar, Long> {
}
