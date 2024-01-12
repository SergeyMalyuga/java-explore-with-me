package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.exploreWithMe.entity.Event;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private int id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
