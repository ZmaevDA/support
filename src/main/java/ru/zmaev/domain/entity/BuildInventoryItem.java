package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "build_inventory_item")
public class BuildInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "build_inventory_item_id_seq")
    @SequenceGenerator(name = "build_inventory_item_id_seq", sequenceName = "build_inventory_item_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", referencedColumnName = "id")
    private InventoryItem inventoryItem;
}
