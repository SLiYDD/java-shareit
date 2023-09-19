package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ResourceAlreadyExistException;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(User user) {
        if (userStorage.findAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ResourceAlreadyExistException("Пользователь с email : " + user.getEmail() + " уже существует");
        }
        return Mapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public UserDto updateUser(User user) {
        findOrThrow(user.getId());
        return Mapper.toUserDto(userStorage.updateUser(user));
    }

    @Override
    public UserDto getUserById(Long userId) {
        return Mapper.toUserDto(findOrThrow(userId));

    }


    private User findOrThrow(long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID : " + userId + " не найден"));
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.findAllUsers().stream()
                .map(Mapper::toUserDto)
                .collect(Collectors.toList());
    }
}
