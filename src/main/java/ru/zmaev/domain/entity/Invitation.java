package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "invitation")
@Getter
@Setter
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitation_id_seq")
    @SequenceGenerator(name = "invitation_id_seq", sequenceName = "invitation_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "build_id", referencedColumnName = "id")
    private Build build;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "invitation", cascade = CascadeType.REMOVE)
    private List<InvitationPrincipal> invitedUsers;
}
