package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "country")
@Getter
@Setter
public class Country {

    @Id
    private Long id;

    @Column(name = "iso")
    private String iso;

    @Column(name = "iso3")
    private String iso3;

    @Column(name = "name")
    private String name;

    @OneToOne(mappedBy = "country", fetch = FetchType.LAZY)
    private User user;
}
