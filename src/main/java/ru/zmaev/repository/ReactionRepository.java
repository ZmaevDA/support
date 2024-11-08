package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.Reaction;
import ru.zmaev.domain.entity.User;
import ru.zmaev.domain.enums.ReactionType;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByBuildAndUser(Build build, User user);

    int countByBuildAndReactionType(Build build, ReactionType reactionType);
}
