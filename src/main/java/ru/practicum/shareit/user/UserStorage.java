package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);

    Optional<User> findUserById(long userId);

    List<User> findAllUsers();

    Optional<User> updateUser(User user);

    void removeUser(long userId);

}
