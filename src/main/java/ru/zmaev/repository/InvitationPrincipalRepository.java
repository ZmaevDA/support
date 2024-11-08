package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.InvitationPrincipal;
import ru.zmaev.domain.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationPrincipalRepository extends JpaRepository<InvitationPrincipal, Long> {
    @Query("SELECT COUNT(ip) > 0 FROM InvitationPrincipal ip WHERE ip.invitation.build = :build AND ip.invitedUser IN :users")
    boolean existsByUsersAndBuild(@Param("users") List<User> users, @Param("build") Build build);

    Optional<InvitationPrincipal> findByInvitedUserIdAndInvitationId(String userId, Long invitationId);
}
