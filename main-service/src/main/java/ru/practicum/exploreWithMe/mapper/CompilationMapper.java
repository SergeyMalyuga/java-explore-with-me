package ru.practicum.exploreWithMe.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.CompilationDto;
import ru.practicum.exploreWithMe.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.entity.Compilation;
import ru.practicum.exploreWithMe.repository.EventRepository;

import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private EventRepository eventRepository;

    public Compilation convertToCompilation(NewCompilationDto compilationDto) {
        return new Compilation().setEvents(eventRepository.findEventsByIds(compilationDto.getEvents()))
                .setTitle(compilationDto.getTitle())
                .setPinned(compilationDto.getPinned());
    }

    public CompilationDto convertToCompilationDto(Compilation compilation) {
        return new CompilationDto().setId(compilation.getId())
                .setEvents(compilation.getEvents().stream().map(e -> eventMapper.convertToEventShortDto(e))
                        .collect(Collectors.toSet()))
                .setPinned(compilation.getPinned())
                .setTitle(compilation.getTitle());
    }
}
