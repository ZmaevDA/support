package ru.zmaev.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "invitation_principal")
@Getter
@Setter
public class InvitationPrincipal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invitation_principal_id_seq")
    @SequenceGenerator(name = "invitation_principal_id_seq", sequenceName = "invitation_principal_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invited_user_id", referencedColumnName = "id")
    private User invitedUser;

    @ManyToOne
    @JoinColumn(name = "invitation_id", referencedColumnName = "id")
    private Invitation invitation;
}
