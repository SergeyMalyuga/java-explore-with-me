package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.client.StatClient;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.entity.*;
import ru.practicum.exploreWithMe.exception.*;
import ru.practicum.exploreWithMe.mapper.EventMapper;
import ru.practicum.exploreWithMe.mapper.RequestMapper;
import ru.practicum.exploreWithMe.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
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
    private StatClient statClient;

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
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest updateEventRequest) {
        Event eventUpdate = updateEventFieldAdmin(eventId, updateEventRequest);
        return eventMapper.convertToEventFullDto(eventRepository.save(eventUpdate));
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(int userId, int eventId,
                                                              EventRequestStatusUpdateRequest updateRequest) {

        Event event = checkTheExistenceEvent(eventId);
        User user = checkTheExistenceUser(userId);

        EventRequestStatusUpdateResult updateResult = new EventRequestStatusUpdateResult();
        List<ParticipationRequest> requestsList = requestRepository.findByRequestIds(updateRequest.getRequestIds());

        checkIsReqStateBeUpdate(updateRequest, requestsList, updateResult, event);
        if (!updateResult.getConfirmedRequests().isEmpty() && !updateResult.getRejectedRequests().isEmpty()) {
            return updateResult;
        }

        changeAllReqStateByResponse(updateRequest, requestsList, updateResult, event);
        return updateResult;

    }

    @Override
    public List<EventShortDto> getAllEventsByCurrentUser(int userId, Integer from, Integer size) {
        return getAllEventsWithFromSizeParam(userId, from, size);
    }

    @Override
    public EventFullDto getEventFullInfo(int userId, int eventId) {
        checkIsUserOwnerByEvent(userId, eventId);
        Event event = eventRepository.findById(eventId).get();
        statClient.setEventView(event);
        return eventMapper.convertToEventFullDto(event);
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
                                size)).getContent().stream()
                .peek(e -> statClient.setEventView(e))
                .map(e -> eventMapper.convertToEventFullDto(e))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventByIdPublic(int eventId, String ip, String uri) {
        Event event = checkTheExistenceEvent(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new AccessErrorException("Event state must be published.");
        }
        statClient.addEventView(ip, uri);
        statClient.setEventView(event);
        return eventMapper.convertToEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getAllEventsPublic(String text, List<Integer> categories, Boolean paid,
                                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                 Boolean onlyAvailable, String sort, Integer from, Integer size,
                                                 String ip, String uri) {

        checkValidDatePublic(rangeStart, rangeEnd);
        statClient.addEventView(ip, uri);
        return sortEventsPublic(sort, text, categories, paid, onlyAvailable, rangeStart, rangeEnd, from, size);
    }

    private List<EventShortDto> getAllEventsWithFromSizeParam(int userId, Integer from, Integer size) {
        return eventRepository.findAllEventsByCurrentUser(userId, PageRequest.of((int)
                        Math.ceil((double) from / size), size)).getContent().stream()
                .peek(e -> statClient.setEventView(e))
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
            throw new InvalidDateException("Field: eventDate. Error: должно содержать дату, которая " +
                    "еще не наступила. Value: " + event.getEventDate());
        }
    }

    private Event updateEventFieldUser(int eventId, UpdateEventUserRequest updateEventRequest) {

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

    private Event updateEventFieldAdmin(int eventId, UpdateEventAdminRequest updateEventRequest) {

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
                throw new RequestException("An event can be published only if it is in the " +
                        "waiting state for publication.");
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


    private void checkIsReqStateBeUpdate(EventRequestStatusUpdateRequest updateRequest,
                                         List<ParticipationRequest> requestsList,
                                         EventRequestStatusUpdateResult updateResult,
                                         Event event) {
        if ((event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0)
                && updateRequest.getStatus().equals(RequestStatus.CONFIRMED.CONFIRMED)) {
            updateResult.setConfirmedRequests(requestsList.stream()
                    .map(e -> requestMapper.convertToResponseRequest(e)).collect(Collectors.toList()));
        }
        if (event.getConfirmedRequests() == event.getParticipantLimit()) {
            throw new RequestException("The limit on applications for this event has been reached.");
        }
    }

    private void changeAllReqStateByResponse(EventRequestStatusUpdateRequest updateRequest,
                                             List<ParticipationRequest> requestsList,
                                             EventRequestStatusUpdateResult updateResult,
                                             Event event) {
        for (ParticipationRequest request : requestsList) {
            if (!request.getRequestStatus().equals(RequestStatus.PENDING)) {
                throw new RequestException("The status can only be changed for applications that " +
                        "are in the waiting state.");
            }
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                request.setRequestStatus(RequestStatus.CANCELED);
            } else {
                request.setRequestStatus(updateRequest.getStatus());
                if (request.getRequestStatus().equals(RequestStatus.CONFIRMED)) {
                    updateResult.getConfirmedRequests().add(requestMapper.convertToResponseRequest(request));
                    incrementConfirmedRequest(event);
                } else {
                    updateResult.getRejectedRequests().add(requestMapper.convertToResponseRequest(request));
                }
            }
            requestRepository.save(request);
        }
    }


    private void checkValidDatePublic(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new InvalidDateException("Even start can`t be after it end.");
        }
    }

    private List<EventFullDto> sortEventsPublic(String sort, String text, List<Integer> categories,
                                                Boolean paid, Boolean onlyAvailable, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Integer from, Integer size) {

        List<EventFullDto> eventsList = eventRepository.findAllEventsPublic(text, categories, paid, rangeStart,
                        rangeEnd, onlyAvailable,
                        PageRequest.of((int) Math.ceil((double) from / size), size)).stream()
                .map(e -> eventMapper.convertToEventFullDto(e)).collect(Collectors.toList());

        if (SortStatus.VIEW.toString().equals(sort)) {
            Collections.sort(eventsList, (e1, e2) -> e2.getViews().compareTo(e1.getViews()));
            return eventsList;
        } else if (SortStatus.EVENT_DATE.toString().equals(sort)) {
            Collections.sort(eventsList, (e1, e2) -> e2.getEventDate().compareTo(e1.getEventDate()));
            return eventsList;
        }

        throw new InvalidDateException("Sorting by " + sort + "is not possible.");
    }

    private void incrementConfirmedRequest(Event event) {
        int count = 1;
        event.setConfirmedRequests(event.getConfirmedRequests() + count);
    }
}


