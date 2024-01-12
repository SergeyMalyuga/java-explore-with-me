package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Set<Integer> events;
    private Boolean pinned;
    @NotBlank
    @Size(message = "Value: Min 1 - Max 50", min = 1, max = 50)
    private String title;
}
