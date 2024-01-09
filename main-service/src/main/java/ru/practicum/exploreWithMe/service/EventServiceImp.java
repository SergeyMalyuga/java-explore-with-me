package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.CategoryRepository;
import ru.practicum.exploreWithMe.dao.EventRepository;
import ru.practicum.exploreWithMe.dao.LocationRepository;
import ru.practicum.exploreWithMe.dao.UserRepository;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;
import ru.practicum.exploreWithMe.entity.User;
import ru.practicum.exploreWithMe.exception.AccessErrorException;
import ru.practicum.exploreWithMe.exception.EventUserUpdateException;
import ru.practicum.exploreWithMe.exception.InvalidDateException;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.mapper.EventMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImp implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    EventMapper eventMapper;

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, int userId) {
        Event event = createEvenByRequestBody(newEventDto, userId);
        locationRepository.save(event.getLocation());
        return eventMapper.convertToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto userUpdateEvent(int userId, int eventId, UpdateEventUserRequest updateEventRequest) { //TODO
        checkIsUserOwnerByEvent(userId, eventId);
        checkValidEventStateForUpdate(eventId);
        Event updateEvent = updateEventField(eventId, updateEventRequest);
        return eventMapper.convertToEventFullDto(eventRepository.save(updateEvent));
    }

    @Override
    public List<EventShortDto> getAllEventsByCurrentUser(int userId, Optional<Integer> from, Optional<Integer> size) {
        if (from.isPresent() && size.isPresent()) {
            return getAllEventsWithFromSizeParam(userId, from, size);
        }
        return new ArrayList<>();
    }

    @Override
    public EventFullDto getEventFullInfo(int userId, int eventId) {
        checkIsUserOwnerByEvent(userId, eventId);
        return eventMapper.convertToEventFullDto(eventRepository.findById(eventId).get());
    }

    private List<EventShortDto> getAllEventsWithFromSizeParam(int userId, Optional<Integer> from,
                                                              Optional<Integer> size) {
        return eventRepository.getAllEventsByCurrentUser(userId, PageRequest.of((int)
                        Math.ceil((double) from.get() / size.get()), size.get())).getContent().stream()
                .map(e -> eventMapper.convertToEventShortDto(e)).collect(Collectors.toList());
    }

    private User checkTheExistenceUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }

    private Event checkTheExistenceEvent(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoDataFoundException("Event with " + eventId
                + " was not found"));
    }

    private void checkIsUserOwnerByEvent(int userId, int evenId) {
        User user = checkTheExistenceUser(userId);
        Event event = checkTheExistenceEvent(evenId);
        int initiatorEventId = event.getInitiator().getId();
        if (initiatorEventId != user.getId()) {
            throw new AccessErrorException("You are not the creator of the event.");
        }
    }

    private void checkValidEventStateForUpdate(int eventId) {
        Event event = eventRepository.findById(eventId).get();
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new EventUserUpdateException("You can only change canceled events or events in the waiting " +
                    "state for moderation");
        }
    }

    private Category checkTheExistenceCategory(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new NoDataFoundException("Category with "
                + categoryId
                + " was not found"));
    }

    private Event createEvenByRequestBody(NewEventDto newEventDto, int userId) {
        Category category = checkTheExistenceCategory(newEventDto.getCategory());
        User user = checkTheExistenceUser(userId);
        Event event = eventMapper.convertToEvent(newEventDto);
        checkValidDate(event);
        event.setInitiator(user).setCategory(category);
        return event;
    }

    private void checkValidDate(Event event) {
        LocalDateTime startEvent = event.getEventDate();
        if (startEvent.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidDateException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + event.getEventDate());
        }
    }

    private Event updateEventField(int eventId, UpdateEventUserRequest updateEventRequest) {
        Event eventFromDb = eventRepository.findById(eventId).get();
        if (updateEventRequest.getRequestModeration() != null) {
            eventFromDb.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getEventDate() != null) {
            eventFromDb.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            eventFromDb.setLocation(updateEventRequest.getLocation());
        }
        if (checkTheExistenceCategory(updateEventRequest.getCategory()) != null) {
            Category category = checkTheExistenceCategory(updateEventRequest.getCategory());
            eventFromDb.setCategory(category);
        }
        if (updateEventRequest.getPaid() != null) {
            eventFromDb.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getDescription() != null) {
            eventFromDb.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getAnnotation() != null) {
            eventFromDb.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            eventFromDb.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getUserStateAction() != null) {
            eventFromDb.setState(convertStateActionToEventState(updateEventRequest.getUserStateAction()));
        }
        if (updateEventRequest.getTitle() != null) {
            eventFromDb.setTitle(updateEventRequest.getTitle());
        }
        checkValidDate(eventFromDb);
        return eventFromDb;
    }

    private EventState convertStateActionToEventState(UserStateAction userStateAction) {
        if (userStateAction.equals(UserStateAction.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        }
        return EventState.PENDING;
    }
}

