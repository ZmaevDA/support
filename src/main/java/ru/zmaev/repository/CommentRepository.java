package ru.zmaev.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.Comment;
import ru.zmaev.domain.entity.User;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByBuildAndUser(Build build, User user);

    @NotNull
    @EntityGraph(
            attributePaths = {
                    "build",
                    "user",
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Page<Comment> findAllByBuild(Build build, @NotNull Pageable pageable);

    int countByBuild(Build build);
}
