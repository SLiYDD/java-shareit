package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ResourceAlreadyExistException;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(User user) {
        try {
            return Mapper.toUserDto(userRepository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("Пользователь с email: " + user.getEmail() + " уже существует!");
        }
    }


    @Override
    public UserDto updateUser(@Valid User user) {
        User oldUser = findUserOrThrow(user.getId());
        if (Objects.isNull(user.getName())) {
            user.setName(oldUser.getName());
        }
        if (Objects.isNull(user.getEmail())) {
            user.setEmail(oldUser.getEmail());
        }
        return Mapper.toUserDto(userRepository.save(user));
    }


    @Override
    public void removeUser(long userId) {
        findUserOrThrow(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return Mapper.toUserDto(findUserOrThrow(userId));

    }

    @Override
    public User findUserOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID : " + userId + " не найден"));
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(Mapper::toUserDto)
                .collect(Collectors.toList());
    }
}
