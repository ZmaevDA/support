package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ashes_of_war")
public class AshesOfWar {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ashes_of_war_id_seq")
    @SequenceGenerator(name = "ashes_of_war_id_seq", sequenceName = "ashes_of_war_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "ashesOfWar", fetch = FetchType.LAZY)
    private List<AshesOfWarWeapon> ashesOfWarWeaponList;
}
