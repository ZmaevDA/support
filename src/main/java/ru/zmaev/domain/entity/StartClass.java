package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "start_class")
public class StartClass {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "start_class_id_seq")
    @SequenceGenerator(name = "start_class_id_seq", sequenceName = "start_class_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "level")
    private Integer level;

    @Column(name = "health")
    private Integer health;

    @Column(name = "mana")
    private Integer mana;

    @Column(name = "stamina")
    private Integer stamina;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private Attribute attribute;

    @OneToMany(mappedBy = "startClass", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Character> characters;
}
