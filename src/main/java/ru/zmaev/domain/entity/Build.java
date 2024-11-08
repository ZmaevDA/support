package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "build")
public class Build {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "build_id_seq")
    @SequenceGenerator(name = "build_id_seq", sequenceName = "build_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_private")
    private Boolean isPrivate;

    @Column(name = "rating")
    private Double rating = 0D;

    @OneToOne(mappedBy = "build", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Invitation invitation;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = 10)
    private Set<Character> characters;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @BatchSize(size = 10)
    @Fetch(FetchMode.SUBSELECT)
    private Set<BuildWeapon> buildWeapons;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @BatchSize(size = 10)
    @Fetch(FetchMode.SUBSELECT)
    private Set<BuildInventoryItem> buildInventoryItems;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY)
    private List<View> views;

    @OneToMany(mappedBy = "build", fetch = FetchType.LAZY)
    private List<Reaction> reactions;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
