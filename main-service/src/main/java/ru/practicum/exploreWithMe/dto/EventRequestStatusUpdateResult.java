package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    List<ResponseRequestDto> confirmedRequests = new ArrayList<>();
    List<ResponseRequestDto> rejectedRequests = new ArrayList<>();

}
