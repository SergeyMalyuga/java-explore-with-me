package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.CompilationRepository;
import ru.practicum.exploreWithMe.dao.EventRepository;
import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.entity.Compilation;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.mapper.CompilationMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public CompilationDto getCompilationById(int compId) {
        Compilation compilation = checkTheExistenceCompilation(compId);
        return compilationMapper.convertToCompilationDto(compilation);
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
        return compilationMapper.convertToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return compilationRepository.findByAllPagination(PageRequest.of((int)
                    Math.ceil((double) from / size), size)).stream().map(e -> compilationMapper
                    .convertToCompilationDto(e)).collect(Collectors.toList());
        }
        return compilationRepository.findByPinned(pinned, PageRequest.of((int)
                Math.ceil((double) from / size), size)).stream().map(e -> compilationMapper
                .convertToCompilationDto(e)).collect(Collectors.toList());
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

    private void updateCompilationField(Compilation compilation, NewCompilationDto newCompilation) { //TODO
        Optional<Set> eventIdsList = Optional.ofNullable(newCompilation.getEvents());
        if (eventIdsList.isPresent()) {
            if (!newCompilation.getEvents().isEmpty()) {
                compilation.getEvents().addAll(eventRepository.findEventsByIds(newCompilation.getEvents()));
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