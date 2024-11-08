package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "principal")
public class User {

    @Id
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "description")
    private String description;

    @Column(name = "age")
    private Integer age;

    @Column(name = "in_game_hours")
    private Integer inGameHours;

    @Column(name = "__deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Build> builds;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<View> views;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reaction> reactions;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
}
