package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.StartClass;

@Repository
public interface StartClassRepository extends JpaRepository<StartClass, Long> {
}
