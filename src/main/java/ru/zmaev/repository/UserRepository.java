package ru.zmaev.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findAll(@NonNull Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}
