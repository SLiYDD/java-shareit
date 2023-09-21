package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
@Builder(toBuilder = true)
public class User {
    private long id;
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Введите валидный email адрес")
    private String email;
}
