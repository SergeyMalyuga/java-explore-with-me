package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.dto.ResponseRequestDto;
import ru.practicum.exploreWithMe.entity.*;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.exception.RequestException;
import ru.practicum.exploreWithMe.mapper.RequestMapper;
import ru.practicum.exploreWithMe.repository.EventRepository;
import ru.practicum.exploreWithMe.repository.RequestRepository;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImp implements RequestService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RequestMapper requestMapper;

    @Override
    public ResponseRequestDto addRequest(int userId, int eventId) {
        ParticipationRequestDto requestDto = createRequestDto(userId, eventId);
        checkValidRequestAdd(userId, eventId);
        changeRequestStatus(eventId, requestDto);
        return addRequestToDataBase(requestDto);

    }

    @Override
    public ResponseRequestDto canceledUserRequest(int userId, int requestId) {
        ParticipationRequest request = checkValidRequestCanceled(userId, requestId);
        return changeStatusAndAddToDataBase(request);
    }

    @Override
    public List<ResponseRequestDto> getUserRequests(int userId) {
        checkTheExistenceUser(userId);
        return requestRepository.getUserRequest(userId).stream()
                .map(e -> requestMapper.convertToResponseRequest(e)).collect(Collectors.toList());
    }

    private User checkTheExistenceUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }

    private Event checkTheExistenceEvent(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NoDataFoundException("Event with " + eventId
                + " was not found"));
    }

    private ParticipationRequest checkTheExistenceRequest(int requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NoDataFoundException("Request with "
                + requestId + " was not found"));
    }

    private void checkValidRequestAdd(int userId, int eventId) {
        Event event = checkTheExistenceEvent(eventId);
        User user = checkTheExistenceUser(userId);
        ParticipationRequest request = requestRepository.searchDuplicateRequest(userId, eventId);
        if (request != null) {
            throw new RequestException("could not execute statement; SQL [n/a]; constraint [uq_request]; " +
                    "nested exception is org.hibernate.exception.ConstraintViolationException: " +
                    "could not execute statement");
        }
        if (event.getInitiator().getId() == user.getId()) {
            throw new RequestException("The initiator of the event cannot add a request to participate in his event");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestException("You cannot participate in an unpublished event");
        }
        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                throw new RequestException("The limit of applications for participation in the event has been reached");
            }
        }
    }

    private ParticipationRequest checkValidRequestCanceled(int userId, int requestId) {
        User user = checkTheExistenceUser(userId);
        ParticipationRequest request = checkTheExistenceRequest(requestId);
        if (request.getRequester().getId() != user.getId()) {
            throw new RequestException("You cannot cancel the application because you are not its creator");
        }
        return request;
    }

    private void changeRequestStatus(int eventId, ParticipationRequestDto requestDto) {
        Event event = checkTheExistenceEvent(eventId);
        if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
            requestDto.setRequestStatus(RequestStatus.CONFIRMED);
        }
    }

    private ParticipationRequestDto createRequestDto(int userId, int eventId) {
        Event event = checkTheExistenceEvent(eventId);
        User user = checkTheExistenceUser(userId);
        return new ParticipationRequestDto().setRequester(user).setEvent(event)
                .setCreated(LocalDateTime.now()).setRequestStatus(RequestStatus.PENDING);
    }

    private ResponseRequestDto addRequestToDataBase(ParticipationRequestDto requestDto) {
        return requestMapper.convertToResponseRequest(requestRepository
                .save(requestMapper.convertToRequest(requestDto)));
    }

    private ResponseRequestDto changeStatusAndAddToDataBase(ParticipationRequest request) {
        request.setRequestStatus(RequestStatus.CANCELED);
        return requestMapper.convertToResponseRequest(requestRepository.save(request));
    }
}
