package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "build_weapon")
public class BuildWeapon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "build_weapon_id_seq")
    @SequenceGenerator(name = "build_weapon_id_seq", sequenceName = "build_weapon_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ashes_of_war_weapon_id", referencedColumnName = "id")
    private AshesOfWarWeapon ashesOfWarWeapon;
}
