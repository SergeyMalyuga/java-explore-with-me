package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.entity.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEventDto, int userId);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventRequest);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventRequest);

    EventRequestStatusUpdateResult updateRequestStatus(int userId, int eventId,
                                                       EventRequestStatusUpdateRequest updateRequest);

    List<EventShortDto> getAllEventsByCurrentUser(int userId, Integer from, Integer size);

    EventFullDto getEventFullInfo(int userId, int eventId);

    List<ResponseRequestDto> getRequestsToEvent(int userId, int eventId);

    List<EventFullDto> getEventsByAdmin(List<Integer> userIdList, List<EventState> eventStateList,
                                        List<Integer> categoryIdList, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto getEventByIdPublic(int eventId, String ip, String uri);

    List<EventFullDto> getAllEventsPublic(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                          Integer size, String ip, String uri);
}
