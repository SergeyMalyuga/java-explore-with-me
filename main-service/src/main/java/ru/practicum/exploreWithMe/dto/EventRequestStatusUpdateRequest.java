package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.exploreWithMe.entity.RequestStatus;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    List<Integer> requestIds;
    RequestStatus status;
}
