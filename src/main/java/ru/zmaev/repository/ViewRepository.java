package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Build;
import ru.zmaev.domain.entity.User;
import ru.zmaev.domain.entity.View;

@Repository
public interface ViewRepository extends JpaRepository<View, Long> {
    boolean existsByBuildAndUser(Build build, User user);

    int countByBuild(Build build);
}
