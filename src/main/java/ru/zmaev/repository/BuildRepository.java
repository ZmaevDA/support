package ru.zmaev.repository;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Build;

import java.util.List;
import java.util.Optional;

@Repository
public interface BuildRepository extends JpaRepository<Build, Long> {

    @NonNull
    @EntityGraph(
            attributePaths = {
                    "characters",
                    "buildWeapons",
                    "buildWeapons.ashesOfWarWeapon",
                    "buildWeapons.ashesOfWarWeapon.weapon",
                    "buildInventoryItems",
                    "buildInventoryItems.inventoryItem",
                    "buildInventoryItems.inventoryItem.resist"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Page<Build> findByIsPrivateFalse(@NonNull Pageable pageable);

    @NonNull
    @EntityGraph(
            attributePaths = {
                    "characters",
                    "buildWeapons",
                    "buildWeapons.ashesOfWarWeapon",
                    "buildWeapons.ashesOfWarWeapon.weapon",
                    "buildInventoryItems",
                    "buildInventoryItems.inventoryItem",
                    "buildInventoryItems.inventoryItem.resist"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    Optional<Build> findById(@NonNull @Param("id") Long id);

    @Query("""
            SELECT b FROM Build b
            JOIN Invitation i ON b.id = i.build.id
            JOIN InvitationPrincipal ip ON i.id = ip.invitation.id
            WHERE ip.invitedUser.id = :userId
            """)
    Page<Build> findBuildsByInvitedUserId(@Param("userId") String userId, Pageable pageable);

    @NonNull
    @EntityGraph(
            attributePaths = {
                    "characters",
                    "buildWeapons",
                    "buildWeapons.ashesOfWarWeapon",
                    "buildWeapons.ashesOfWarWeapon.weapon",
                    "buildInventoryItems",
                    "buildInventoryItems.inventoryItem",
                    "buildInventoryItems.inventoryItem.resist"
            },
            type = EntityGraph.EntityGraphType.LOAD
    )
    @Query("SELECT b FROM Build b WHERE b.id IN :ids")
    List<Build> findAllById(@Param("ids") List<Long> ids);
}
