package ru.practicum.exploreWithMe.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.dto.EventShortDto;
import ru.practicum.exploreWithMe.dto.NewEventDto;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final LocationMapper locationMapper;

    public EventFullDto convertToEventFullDto(Event event) {
        return new EventFullDto().setEventDate(event.getEventDate())
                .setAnnotation(event.getAnnotation())
                .setId(event.getId())
                .setCategory(categoryMapper.convertToCategoryDto(event.getCategory()))
                .setCreatedOn(event.getCreatedOn())
                .setInitiator(userMapper.convertToUserShortDto(event.getInitiator()))
                .setDescription(event.getDescription())
                .setPaid(event.isPaid())
                .setConfirmedRequests(event.getConfirmedRequests())
                .setLocation(locationMapper.convertToLocationDto(event.getLocation()))
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn())
                .setRequestModeration(event.getRequestModeration())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public Event convertToEvent(NewEventDto newEventDto) {
        Event event = new Event().setAnnotation(newEventDto.getAnnotation())
                .setDescription(newEventDto.getDescription())
                .setEventDate(newEventDto.getEventDate())
                .setLocation(newEventDto.getLocation())
                .setCreatedOn(LocalDateTime.now())
                .setState(EventState.PENDING)
                .setPaid(newEventDto.isPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit())
                .setTitle(newEventDto.getTitle());
        if (newEventDto.getRequestModeration() != null) {
            event.setRequestModeration(newEventDto.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }
        return event;
    }

    public EventShortDto convertToEventShortDto(Event event) {
        return new EventShortDto().setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(categoryMapper.convertToCategoryDto(event.getCategory()))
                .setConfirmedRequests(event.getConfirmedRequests())
                .setEventDate(event.getEventDate())
                .setInitiator(userMapper.convertToUserShortDto(event.getInitiator()))
                .setPaid(event.isPaid())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }
}
