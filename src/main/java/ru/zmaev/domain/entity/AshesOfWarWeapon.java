package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ashes_of_war_weapon")
public class AshesOfWarWeapon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ashes_of_war_weapon_id_seq")
    @SequenceGenerator(name = "ashes_of_war_weapon_id_seq", sequenceName = "ashes_of_war_weapon_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_id", referencedColumnName = "id")
    private Weapon weapon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ashes_of_war_id", referencedColumnName = "id")
    private AshesOfWar ashesOfWar;

    @OneToMany(mappedBy = "ashesOfWarWeapon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BuildWeapon> buildWeapons;
}
