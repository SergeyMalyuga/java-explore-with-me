package ru.practicum.exploreWithMe.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.UserDto;
import ru.practicum.exploreWithMe.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@Valid @RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(name = "id") int userId) {
        userService.removeUser(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids", required = false) List<Integer> uid,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                  Optional<Integer> from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10")
                                  Optional<Integer> size) {
        return userService.getUsers(uid, from, size);
    }
}
