package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto addUser(UserDto userDto);

    void removeUser(int userId);

    List<UserDto> getUsers(List<Integer> uid, Optional<Integer> from, Optional<Integer> size);

}
