package ru.practicum.exploreWithMe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {


    private int id;
    @NotNull(message = "The \"name\" field cannot be empty.")
    @NotBlank(message = "The \"name\" field cannot be empty.")
    @Size(message = "Value: Min 2 / Max value 250", min = 2, max = 250)
    private String name;
    @Email(message = "Invalid email format")
    @NotBlank(message = "The \"email\" field cannot be empty.")
    @Size(message = "Value: Min 6 - Max 254", min = 6, max = 254)
    private String email;
}
