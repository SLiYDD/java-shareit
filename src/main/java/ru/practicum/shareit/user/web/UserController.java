package ru.practicum.shareit.user.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.info("Получен POST-запрос: '/users' на добавление пользователя");
        return userService.createUser(Mapper.toUser(userDto));
    }


    @PatchMapping("/{userId}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Получен PATCH-запрос: '/users' на обновление пользователя с ID={}", userId);
        var user = Mapper.toUser(userDto);
        return userService.updateUser(user.toBuilder().id(userId).build());
    }


    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userService.getUserById(userId);
    }


    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Получен DELETE-запрос: '/users' на удаление пользователя с ID={}", userId);
        userService.removeUser(userId);
    }


    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
