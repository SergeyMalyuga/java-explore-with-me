package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.exploreWithMe.entity.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRequestDto {
    private int id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private int event;
    private int requester;
    private RequestStatus status;
}
