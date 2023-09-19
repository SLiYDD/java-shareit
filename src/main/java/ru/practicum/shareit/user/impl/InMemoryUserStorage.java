package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private static long countId = 0L;

    @Override
    public User createUser(User user) {
        user.setId(++countId);
        users.put(countId, user);
        return users.get(countId);
    }

    @Override
    public Optional<User> findUserById(long userId) {
        if (users.containsKey(userId)) {
            return Optional.of(users.get(userId));
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        var userId = user.getId();
        var updateUser = users.get(userId);
        if (Objects.nonNull(user.getName())) {
            updateUser.setName(user.getName());
        }

        if (Objects.nonNull(user.getEmail())) {
            updateUser.setEmail(user.getEmail());
        }

        users.put(userId, updateUser);
        return users.get(userId);
    }


}
