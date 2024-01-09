package ru.practicum.exploreWithMe.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.dto.EventShortDto;
import ru.practicum.exploreWithMe.dto.NewEventDto;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;

@Component
public class EventMapper {

    @Autowired
    private UserMapper userMapper;

    public EventFullDto convertToEventFullDto(Event event) {
        return new EventFullDto().setEventDate(event.getEventDate())
                .setAnnotation(event.getAnnotation())
                .setId(event.getId())
                .setCategory(event.getCategory())
                .setCreatedOn(event.getCreatedOn())
                .setInitiator(userMapper.convertToUserShortDto(event.getInitiator()))
                .setDescription(event.getDescription())
                .setPaid(event.isPaid())
                .setConfirmedRequests(event.getConfirmedRequests())
                .setLocation(event.getLocation())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublishedOn())
                .setRequestModeration(event.isRequestModeration())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public Event convertToEvent(NewEventDto newEventDto) {
        return new Event().setAnnotation(newEventDto.getAnnotation())
                .setDescription(newEventDto.getDescription())
                .setEventDate(newEventDto.getEventDate())
                .setLocation(newEventDto.getLocation())
                .setCreatedOn(LocalDateTime.now())
                .setState(EventState.PENDING)
                .setPaid(newEventDto.isPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit())
                .setRequestModeration(newEventDto.isRequestModeration())
                .setTitle(newEventDto.getTitle());
    }

    public EventShortDto convertToEventShortDto(Event event) {
        return new EventShortDto().setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(event.getCategory())
                .setConfirmedRequests(event.getConfirmedRequests())
                .setEventDate(event.getEventDate())
                .setInitiator(userMapper.convertToUserShortDto(event.getInitiator()))
                .setPaid(event.isPaid())
                .setTitle(event.getTitle())
                .setViews(event.getViews());

    }
}
