package ru.practicum.exploreWithMe.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.exploreWithMe.dto.UserDto;
import ru.practicum.exploreWithMe.dto.UserShotDto;
import ru.practicum.exploreWithMe.entity.User;

@Component
public class UserMapper {

    public User convertToUser(UserDto userDto) {
        return new User().setName(userDto.getName()).setEmail(userDto.getEmail());
    }

    public UserDto convertToUserDto(User user) {
        return new UserDto().setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail());
    }

    public UserShotDto convertToUserShortDto(User user) {
        return new UserShotDto().setId(user.getId()).setName(user.getName());
    }
}
