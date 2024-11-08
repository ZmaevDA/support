package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Attribute;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute, Long> {
}
