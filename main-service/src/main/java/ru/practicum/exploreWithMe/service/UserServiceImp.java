package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
        return getUsersWithPagination(uid, from, size);
    }

    private void checkTheExistenceUser(int userId) {
        userRepository.findById(userId).orElseThrow(() -> new NoDataFoundException("User with " + userId
                + " was not found"));
    }

    private List<UserDto> getUsersWithPagination(List<Integer> uid, Integer from, Integer size) {
        return userRepository.findAllUsersWithPagination(uid, PageRequest.of((int) Math.ceil((double) from / size),
                size)).stream().map(e -> userMapper.convertToUserDto(e)).collect(Collectors.toList());
    }

    private void checkAvailableEmail(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new RequestException("Email is already exists.");
        }
    }
}
