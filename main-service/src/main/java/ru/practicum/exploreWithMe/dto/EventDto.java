package ru.practicum.exploreWithMe.dto;

import ru.practicum.exploreWithMe.entity.Category;
import ru.practicum.exploreWithMe.entity.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class EventDto {

    private int id;
    @Size(min = 20, max = 2000)
    private String annotation;
    private Category category;
    @Size(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
}

