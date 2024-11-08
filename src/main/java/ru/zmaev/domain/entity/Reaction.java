package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.zmaev.domain.enums.ReactionType;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "reaction")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reaction_id_seq")
    @SequenceGenerator(name = "reaction_id_seq", sequenceName = "reaction_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type")
    private ReactionType reactionType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "reacted_at")
    private Instant reactedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;
}
