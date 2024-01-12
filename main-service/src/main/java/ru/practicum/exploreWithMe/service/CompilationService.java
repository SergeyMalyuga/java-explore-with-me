package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilation);

    void removeCompilation(int compId);

    CompilationDto updateCompilation(int compId, NewCompilationDto newCompilation);
}
