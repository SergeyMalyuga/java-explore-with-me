package ru.practicum.exploreWithMe.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class EventRequestStatusUpdateRequest {

    List<Integer> requestIds;
    RequestState state;
}
