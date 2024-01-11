package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEventDto, int userId);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventRequest);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventRequest);

    List<EventShortDto> getAllEventsByCurrentUser(int userId, Integer from, Integer size);

    EventFullDto getEventFullInfo(int userId, int eventId);

    List<ResponseRequestDto> getRequestsToEvent(int userId, int eventId);

    List<EventFullDto> getEventsByAdmin(List<Integer> userIdList, List<EventState> eventStateList,
                                        List<Integer> categoryIdList, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Integer from, Integer size);
}