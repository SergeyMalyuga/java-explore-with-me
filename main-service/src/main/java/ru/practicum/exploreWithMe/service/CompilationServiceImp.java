package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.CompilationRepository;
import ru.practicum.exploreWithMe.dao.EventRepository;
import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.entity.Compilation;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.mapper.CompilationMapper;

import java.util.Optional;
import java.util.Set;

@Service
public class CompilationServiceImp implements CompilationService {

    @Autowired
    private CompilationRepository compilationRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CompilationMapper compilationMapper;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilation) {
        checkPinnedValue(newCompilation);
        return compilationMapper.convertToCompilationDto(compilationRepository
                .save(compilationMapper.convertToCompilation(newCompilation)));
    }

    @Override
    public void removeCompilation(int compId) {
        checkTheExistenceCompilation(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(int compId, NewCompilationDto newCompilation) {
        Compilation compilation = checkTheExistenceCompilation(compId);
        updateCompilationField(compilation, newCompilation);
        return compilationMapper.convertToCompilationDto(compilation);
    }

    private void checkPinnedValue(NewCompilationDto newCompilation) {
        if (newCompilation.getPinned() == null) {
            newCompilation.setPinned(false);
        }
    }

    private Compilation checkTheExistenceCompilation(int compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NoDataFoundException("Compilation with " + compId
                + " was not found"));
    }

    private void updateCompilationField(Compilation compilation, NewCompilationDto newCompilation) {
        Optional<Set> eventIdsList = Optional.ofNullable(newCompilation.getEvents());
        if (eventIdsList.isPresent()) {
            if (!newCompilation.getEvents().isEmpty()) {
                compilation.setEvents(eventRepository.findEventsByIds(newCompilation.getEvents()));
            }
        }
        if (newCompilation.getPinned() != null) {
            compilation.setPinned(newCompilation.getPinned());
        }
        if (newCompilation.getTitle() != null) {
            compilation.setTitle(newCompilation.getTitle());
        }
    }
}
