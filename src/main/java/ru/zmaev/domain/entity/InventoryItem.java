package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.zmaev.domain.enums.ItemType;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "inventory_item")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_item_id_seq")
    @SequenceGenerator(name = "inventory_item_id_seq", sequenceName = "inventory_item_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "item_type")
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "resist_id", referencedColumnName = "id")
    private Resist resist;

    @OneToMany(mappedBy = "inventoryItem", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BuildInventoryItem> buildInventoryItems;
}
