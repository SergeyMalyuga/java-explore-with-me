package ru.practicum.exploreWithMe.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.entity.Compilation;

import java.util.Set;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

}
