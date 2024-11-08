package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Weapon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weapon_id_seq")
    @SequenceGenerator(name = "weapon_id_seq", sequenceName = "weapon_id_seq", allocationSize = 1)
    private Long id;

    @OneToMany(mappedBy = "weapon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<AshesOfWarWeapon> ashesOfWarWeaponList;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "weight")
    private Double weight;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private Attribute attribute;
}
