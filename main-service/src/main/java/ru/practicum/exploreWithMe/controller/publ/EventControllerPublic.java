package ru.practicum.exploreWithMe.controller.publ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.entity.SortStatus;
import ru.practicum.exploreWithMe.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventControllerPublic {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEvents(@RequestParam(name = "text", required = false) String text,
                                           @RequestParam(name = "categories", required = false) List<Integer> categories,
                                           @RequestParam(name = "paid", required = false) Boolean paid,
                                           @RequestParam(name = "rangeStart", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                           @RequestParam(name = "rangeEnd", required = false)
                                           @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                           @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                           @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        return eventService.getAllEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(name = "id") int eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request.getRemoteAddr(), request.getRequestURI());
    }
}
