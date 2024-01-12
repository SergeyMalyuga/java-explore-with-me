package ru.practicum.exploreWithMe.controller.publ;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.exploreWithMe.dto.EventFullDto;
import ru.practicum.exploreWithMe.service.EventService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/events")
public class EventControllerPublic {

    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public EventFullDto getEventById(@PathVariable(name = "id") int eventId, HttpServletRequest request) {
        return eventService.getEventByIdPublic(eventId, request.getRemoteAddr(), request.getRequestURI());
    }
}
