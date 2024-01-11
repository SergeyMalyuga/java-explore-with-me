package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.*;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.entity.Event;
import ru.practicum.exploreWithMe.entity.EventState;
import ru.practicum.exploreWithMe.entity.User;
import ru.practicum.exploreWithMe.exception.*;
import ru.practicum.exploreWithMe.mapper.EventMapper;
import ru.practicum.exploreWithMe.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImp implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    EventMapper eventMapper;

    @Autowired
    RequestMapper requestMapper;

    @Override
    public EventFullDto addEvent(NewEventDto newEventDto, int userId) {
        Event event = createEvenByRequestBody(newEventDto, userId);
        locationRepository.save(event.getLocation());
        return eventMapper.convertToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventRequest) {
        checkIsUserOwnerByEvent(userId, eventId);
        checkValidEventStateForUpdate(eventId);
        Event updateEvent = updateEventFieldUser(eventId, updateEventRequest);
        return eventMapper.convertToEventFullDto(eventRepository.save(updateEvent));
    }

    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventRequest) { //TODO create method
        Event eventUpdate = updateEventFieldAdmin(eventId, updateEventRequest);
        return eventMapper.convertToEventFullDto(eventRepository.save(eventUpdate));
    }

    @Override
    public List<EventShortDto> getAllEventsByCurrentUser(int userId, Integer from, Integer size) {
        return getAllEventsWithFromSizeParam(userId, from, size);
    }

    @Override
    public EventFullDto getEventFullInfo(int userId, int eventId) {
        checkIsUserOwnerByEvent(userId, eventId);
        return eventMapper.convertToEventFullDto(eventRepository.findById(eventId).get());
    }

    @Override
    public List<ResponseRequestDto> getRequestsToEvent(int userId, int eventId) {
        checkIsUserOwnerByEvent(userId, eventId);
        return requestRepository.findAllRequestsByEventId(eventId).stream()
                .map(e -> requestMapper.convertToResponseRequest(e)).collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(List<Integer> userIdList, List<EventState> eventStateList,
                                               List<Integer> categoryIdList, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, Integer from,
                                               Integer size) {
        return eventRepository.findEventsByAdmin(userIdList, eventStateList, categoryIdList, rangeStart, rangeEnd,
                        PageRequest.of((int) Math.ceil((double) from / size),
                                size)).getContent().stream().map(e -> eventMapper.convertToEventFullDto(e))
                .collect(Collectors.toList());
    }

    private List<EventShortDto> getAllEventsWithFromSizeParam(int userId, Integer from, Integer size) {
        return eventRepository.findAllEventsByCurrentUser(userId, PageRequest.of((int)
                        Math.ceil((double) from / size), size)).getContent().stream()
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

    private Event updateEventFieldUser(int eventId, UpdateEventUserRequest updateEventRequest) { //TODO разбить на мелкие методы

        Event eventFromDb = eventRepository.findById(eventId).get();
        if (updateEventRequest.getRequestModeration() != null) {
            eventFromDb.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getEventDate() != null) {
            eventFromDb.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            eventFromDb.setLocation(updateEventRequest.getLocation());
            locationRepository.save(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getCategory() != 0) {
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
        if (updateEventRequest.getStateAction() != null) {
            eventFromDb.setState(convertStateActionToEventState(updateEventRequest.getStateAction()));
        }
        if (updateEventRequest.getTitle() != null) {
            eventFromDb.setTitle(updateEventRequest.getTitle());
        }
        checkValidDate(eventFromDb);
        return eventFromDb;
    }

    private Event updateEventFieldAdmin(int eventId, UpdateEventAdminRequest updateEventRequest) { //TODO разбить на мелкие методы

        Event eventFromDb = checkTheExistenceEvent(eventId);
        if (updateEventRequest.getRequestModeration() != null) {
            eventFromDb.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidDateException("The start date of the event to be modified must be no earlier " +
                        "than one hour from the date of publication.");
            }
            eventFromDb.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            eventFromDb.setLocation(updateEventRequest.getLocation());
            locationRepository.save(updateEventRequest.getLocation());
        }
        if (updateEventRequest.getCategory() != 0) {
            Category categoryNew = checkTheExistenceCategory(updateEventRequest.getCategory());
            eventFromDb.setCategory(categoryNew);
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
        if (updateEventRequest.getStateAction() != null) {
            if (!eventFromDb.getState().equals(EventState.PENDING) && updateEventRequest.getStateAction()
                    .equals(AdminStateAction.PUBLISH_EVENT)) {
                throw new RequestException("An event can be published only if it is in the waiting state for publication.");
            } else if (eventFromDb.getState().equals(EventState.PUBLISHED) && updateEventRequest.getStateAction()
                    .equals(AdminStateAction.REJECT_EVENT)) {
                throw new RequestException("An event can be rejected only if it has not been published yet.");
            }
            eventFromDb.setState(convertAdminStateToEvenState(updateEventRequest.getStateAction()));
            if (eventFromDb.getState().equals(EventState.PUBLISHED)) {
                eventFromDb.setPublishedOn(LocalDateTime.now());
            }
        }
        if (updateEventRequest.getTitle() != null) {
            eventFromDb.setTitle(updateEventRequest.getTitle());
        }
        return eventFromDb;
    }

    private EventState convertStateActionToEventState(UserStateAction userStateAction) {
        if (userStateAction.equals(UserStateAction.CANCEL_REVIEW)) {
            return EventState.CANCELED;
        }
        return EventState.PENDING;
    }

    private EventState convertAdminStateToEvenState(AdminStateAction stateAction) {
        if (stateAction.equals(AdminStateAction.PUBLISH_EVENT)) {
            return EventState.PUBLISHED;
        }
        return EventState.CANCELED;
    }
}

