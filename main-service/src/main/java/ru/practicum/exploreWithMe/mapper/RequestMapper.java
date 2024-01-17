package ru.practicum.exploreWithMe.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.ResponseRequestDto;
import ru.practicum.exploreWithMe.entity.ParticipationRequest;

@Component
public class RequestMapper {

    public ResponseRequestDto convertToResponseRequest(ParticipationRequest request) {
        return new ResponseRequestDto().setId(request.getId())
                .setEvent(request.getEvent().getId())
                .setCreated(request.getCreated())
                .setStatus(request.getRequestStatus())
                .setRequester(request.getRequester().getId());
    }
}
