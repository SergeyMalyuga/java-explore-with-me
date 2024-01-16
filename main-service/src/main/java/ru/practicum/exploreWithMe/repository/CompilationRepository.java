package ru.practicum.exploreWithMe.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.exploreWithMe.entity.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "SELECT c FROM Compilation AS c WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    List<Compilation> findByAllPagination(@Param("pinned") Boolean pinned, Pageable pageable);

}
