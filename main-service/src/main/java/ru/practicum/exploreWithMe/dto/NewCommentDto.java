package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    @NotNull(groups = {Marker.OnUpdate.class, Marker.OnCreate.class})
    private String text;
    @NotNull(groups = Marker.OnCreate.class)
    private Integer event;
}
