package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(User user);

    UserDto getUserById(Long userId);

    User findUserOrThrow(long userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(User user);

    void removeUser(long userId);
}
