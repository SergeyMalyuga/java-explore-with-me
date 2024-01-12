package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.entity.EventState;
import ru.practicum.exploreWithMe.entity.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {


    private int id;
    private String annotation;
    private Category category;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    private String title;
    private int confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private UserShotDto initiator;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private EventState state;
    private int views;
}

