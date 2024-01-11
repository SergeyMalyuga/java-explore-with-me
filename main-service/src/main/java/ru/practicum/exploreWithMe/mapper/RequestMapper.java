package ru.practicum.exploreWithMe.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.dto.ResponseRequestDto;
import ru.practicum.exploreWithMe.entity.ParticipationRequest;

@Component
public class RequestMapper {

    public ParticipationRequest convertToRequest(ParticipationRequestDto requestDto) {
        return new ParticipationRequest().setCreated(requestDto.getCreated())
                .setRequester(requestDto.getRequester())
                .setRequestStatus(requestDto.getRequestStatus())
                .setEvent(requestDto.getEvent());
    }

    public ParticipationRequestDto convertToRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto().setId(request.getId())
                .setCreated(request.getCreated())
                .setRequester(request.getRequester())
                .setRequestStatus(request.getRequestStatus())
                .setEvent(request.getEvent());
    }

    public ResponseRequestDto convertToResponseRequest(ParticipationRequest request) {
        return new ResponseRequestDto().setId(request.getId())
                .setEvent(request.getEvent().getId())
                .setCreated(request.getCreated())
                .setStatus(request.getRequestStatus())
                .setRequester(request.getRequester().getId());
    }
}
