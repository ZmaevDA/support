package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.zmaev.domain.entity.BuildInventoryItem;

public interface BuildInventoryItemRepository extends JpaRepository<BuildInventoryItem, Long> {
    void deleteAllByBuildId(Long buildId);
}
