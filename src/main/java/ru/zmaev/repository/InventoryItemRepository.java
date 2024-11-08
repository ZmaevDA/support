package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.InventoryItem;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
}
