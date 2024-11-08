package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zmaev.domain.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
