package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "view")
public class View {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "view_id_seq")
    @SequenceGenerator(name = "view_id_seq", sequenceName = "view_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;

    @Column(name = "viewed_at")
    private Instant viewedAt = Instant.now();
}
