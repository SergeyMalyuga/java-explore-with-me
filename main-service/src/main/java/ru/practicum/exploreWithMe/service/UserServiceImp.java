package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dao.UserRepository;
import ru.practicum.exploreWithMe.dto.UserDto;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.mapper.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        return userMapper.convertToUserDto(userRepository.save(userMapper.convertToUser(userDto)));
    }

    @Override
    public void removeUser(int userId) {
        checkTheExistenceUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> uid, Optional<Integer> from, Optional<Integer> size) {
        if (uid != null) {
            return userRepository.findAllUsers(uid).stream().map(e -> userMapper.convertToUserDto(e))
                    .collect(Collectors.toList());
        } else if (from.isPresent() && size.isPresent()) { //TODO проверка в контроллере на positive or zero
            return userRepository.findAllUsersWithPagination(PageRequest.of(
                            (int) Math.ceil((double) from.get() / size.get()),
                            size.get())).getContent().stream().map(e -> userMapper.convertToUserDto(e))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private void checkTheExistenceUser(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }
}
