package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilation);
    CompilationDto getCompilationById(int compId);

    void removeCompilation(int compId);

    CompilationDto updateCompilation(int compId, NewCompilationDto newCompilation);
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size );
}
