package ru.practicum.exploreWithMe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.dto.UserDto;
import ru.practicum.exploreWithMe.exception.NoDataFoundException;
import ru.practicum.exploreWithMe.exception.RequestException;
import ru.practicum.exploreWithMe.mapper.UserMapper;
import ru.practicum.exploreWithMe.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDto addUser(UserDto userDto) {
        checkAvailableEmail(userDto);
        return userMapper.convertToUserDto(userRepository.save(userMapper.convertToUser(userDto)));
    }

    @Override
    public void removeUser(int userId) {
        checkTheExistenceUser(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Integer> uid, Integer from, Integer size) {
        if (uid != null) {
            return getUsersWithUid(uid, from, size);
        }
        return getUsersWithoutUid(from, size);
    }

    private void checkTheExistenceUser(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }

    private List<UserDto> getUsersWithUid(List<Integer> uid, Integer from, Integer size) {
        return userRepository.findAllUsersWithUid(uid, PageRequest.of((int) Math.ceil((double) from / size), size))
                .stream().map(e -> userMapper.convertToUserDto(e)).collect(Collectors.toList());
    }

    private List<UserDto> getUsersWithoutUid(Integer from, Integer size) {
        return userRepository.findAllUsersWithoutUid(PageRequest.of((int) Math.ceil((double) from / size), size))
                .stream().map(e -> userMapper.convertToUserDto(e)).collect(Collectors.toList());
    }

    private void checkAvailableEmail(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RequestException("Email is already exists.");
        }
    }
}
