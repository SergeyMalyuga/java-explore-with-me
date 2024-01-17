package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.ResponseRequestDto;

import java.util.List;

public interface RequestService {

    ResponseRequestDto addRequest(int userId, int eventId);

    ResponseRequestDto canceledUserRequest(int userId, int requestId);

    List<ResponseRequestDto> getUserRequests(int userId);
}
