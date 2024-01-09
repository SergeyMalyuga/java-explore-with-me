package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.dto.EventShortDto;
import ru.practicum.exploreWithMe.dto.NewEventDto;
import ru.practicum.exploreWithMe.dto.UpdateEventUserRequest;

import java.util.List;
import java.util.Optional;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEventDto, int userId);
    EventFullDto userUpdateEvent(int userId, int eventId, UpdateEventUserRequest updateEventRequest);

    List<EventShortDto> getAllEventsByCurrentUser(int userId, Optional<Integer> from, Optional<Integer> size);

    EventFullDto getEventFullInfo(int userId, int eventId);
}
