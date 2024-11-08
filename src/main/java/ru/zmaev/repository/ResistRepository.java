package ru.zmaev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.zmaev.domain.entity.Resist;

@Repository
public interface ResistRepository extends JpaRepository<Resist, Long> {
    @Query("""
            SELECT NEW Resist(
            CAST(SUM(r.vitality) AS Integer),
            CAST(SUM(r.focus) AS Integer),
            CAST(SUM(r.robustness) AS Integer),
            CAST(SUM(r.immunity) AS Integer),
            CAST(SUM(r.holy) AS Integer),
            CAST(SUM(r.lightning) AS Integer),
            CAST(SUM(r.fire) AS Integer),
            CAST(SUM(r.magic) AS Integer),
            CAST(SUM(r.pierce) AS Integer),
            CAST(SUM(r.splash) AS Integer),
            CAST(SUM(r.strike) AS Integer),
            CAST(SUM(r.physical) AS Integer))
            FROM BuildInventoryItem bii
            JOIN bii.inventoryItem ii
            JOIN ii.resist r
            WHERE bii.build.id = :buildId""")
    Resist findSumOfResistsByBuildId(@Param("buildId") Long buildId);
}
