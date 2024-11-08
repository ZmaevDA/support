package ru.zmaev.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = "attribute")
    Page<Character> findAll(@NonNull Pageable pageable);
}
