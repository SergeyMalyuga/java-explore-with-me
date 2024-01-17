package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    void removeUser(int userId);

    List<UserDto> getUsers(List<Integer> uid, Integer from, Integer size);

}
