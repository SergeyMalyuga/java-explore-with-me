package ru.practicum.exploreWithMe.controller.priv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
public class EventControllerPrivate {

    @Autowired
    private EventService eventService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable(name = "userId") int userId) {
        return eventService.addEvent(newEventDto, userId);
    }

    @GetMapping()
    public List<EventShortDto> getAllEventsByCurrentUser(@PathVariable(name = "userId") int userId,
                                                         @PositiveOrZero @RequestParam(name = "from",
                                                                 defaultValue = "0")
                                                         Integer from,
                                                         @Positive @RequestParam(name = "size",
                                                                 defaultValue = "10")
                                                         Integer size) {
        return eventService.getAllEventsByCurrentUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventFullInfo(@PathVariable(name = "userId") int userId,
                                         @PathVariable(name = "eventId") int eventId) {
        return eventService.getEventFullInfo(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto userUpdateEvent(@PathVariable(name = "userId") int userId,
                                        @PathVariable(name = "eventId") int eventId,
                                        @Valid @RequestBody UpdateEventUserRequest updateEventRequest) {
        return eventService.updateEventByUser(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ResponseRequestDto> getRequestsToEvent(@PathVariable(name = "userId") int userId,
                                                       @PathVariable(name = "eventId") int eventId) {
        return eventService.getRequestsToEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestStatus(@PathVariable(name = "userId") int userId,
                                                              @PathVariable(name = "eventId") int eventId,
                                                              @RequestBody
                                                              EventRequestStatusUpdateRequest updateRequest) {
        return eventService.updateRequestStatus(userId, eventId, updateRequest);
    }
}
