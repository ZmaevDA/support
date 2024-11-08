package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    boolean existsByBuildId(Long buildId);
}
