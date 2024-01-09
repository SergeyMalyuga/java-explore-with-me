package ru.practicum.exploreWithMe.controller.priv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.dto.EventShortDto;
import ru.practicum.exploreWithMe.dto.NewEventDto;
import ru.practicum.exploreWithMe.dto.UpdateEventUserRequest;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class PrivateEventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvent(@Valid @RequestBody NewEventDto newEventDto,
                                  @PathVariable(name = "userId") int userId) {
        return eventService.addEvent(newEventDto, userId);
    }

    @GetMapping("{userId}/events")
    public List<EventShortDto> getAllEventsByCurrentUser(@PathVariable(name = "userId") int userId,
                                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                         Optional<Integer> from,
                                                         @Positive @RequestParam(name = "size", defaultValue = "10")
                                                         Optional<Integer> size) {
        return eventService.getAllEventsByCurrentUser(userId, from, size);
    }

    @GetMapping("{userId}/events/{eventId}")
    public EventFullDto getEventFullInfo(@PathVariable(name = "userId") int userId,
                                         @PathVariable(name = "eventId") int eventId) {
        return eventService.getEventFullInfo(userId, eventId);
    }

    @PatchMapping("{userId}/events/{eventId}")
    public EventFullDto userUpdateEvent(@PathVariable(name = "userId") int userId,
                                        @PathVariable(name = "eventId") int eventId,
                                        @Valid @RequestBody UpdateEventUserRequest updateEventRequest) {
        return eventService.userUpdateEvent(userId, eventId, updateEventRequest);
    }
}
