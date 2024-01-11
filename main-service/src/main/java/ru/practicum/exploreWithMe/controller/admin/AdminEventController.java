package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.dto.UpdateEventAdminRequest;
import ru.practicum.exploreWithMe.entity.EventState;
import ru.practicum.exploreWithMe.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
public class AdminEventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(@RequestParam(name = "users", required = false)
                                               List<Integer> userIdList,
                                               @RequestParam(name = "states", required = false)
                                               List<EventState> eventStateList,
                                               @RequestParam(name = "categories", required = false)
                                               List<Integer> categoryIdList,
                                               @RequestParam(name = "rangeStart", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeStart,
                                               @RequestParam(name = "rangeEnd", required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               LocalDateTime rangeEnd,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                               Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10")
                                               Integer size) {

        return eventService.getEventsByAdmin(userIdList, eventStateList, categoryIdList, rangeStart,
                rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventsByAdmin(@PathVariable(name = "eventId") int eventId,
                                            @Valid @RequestBody UpdateEventAdminRequest request) {
        return eventService.updateEventByAdmin(eventId, request);
    }
}
