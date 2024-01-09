package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private int id;
    @NotBlank
    @Size(message = "Value: Min 1 - Max 50", min = 1, max = 50)
    private String name;
}
