package ru.practicum.exploreWithMe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.practicum.exploreWithMe.entity.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    @Size(message = "Value: Min 20 / Max value 2000", min = 20, max = 2000)
    private String annotation;
    @NotNull
    private int category;
    @NotNull
    @Size(message = "Value: Min 20 / Max value 7000", min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @NotNull
    private boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(message = "Value: Min 3 / Max value 120", min = 3, max = 120)
    private String title;
}
