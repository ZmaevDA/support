package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "attribute")
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attribute_id_seq")
    @SequenceGenerator(name = "attribute_id_seq", sequenceName = "attribute_id_seq", allocationSize = 1, initialValue = 11)
    private Long id;

    @Column(name = "vigor")
    private Integer vigor;

    @Column(name = "mind")
    private Integer mind;

    @Column(name = "endurance")
    private Integer endurance;

    @Column(name = "strength")
    private Integer strength;

    @Column(name = "dexterity")
    private Integer dexterity;

    @Column(name = "intelligence")
    private Integer intelligence;

    @Column(name = "faith")
    private Integer faith;

    @Column(name = "arcana")
    private Integer arcana;

    @OneToOne(mappedBy = "attribute", fetch = FetchType.EAGER)
    private Character character;

    @OneToOne(mappedBy = "attribute", fetch = FetchType.EAGER)
    private StartClass startClass;

    @OneToOne(mappedBy = "attribute", fetch = FetchType.EAGER)
    private Weapon weapon;
}
