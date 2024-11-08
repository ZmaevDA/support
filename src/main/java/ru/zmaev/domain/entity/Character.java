package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "character")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_id_seq")
    @SequenceGenerator(name = "character_id_seq", sequenceName = "character_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "attribute_id", referencedColumnName = "id")
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "start_class_id", referencedColumnName = "id")
    private StartClass startClass;
}
