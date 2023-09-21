package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private long id;
    @NotBlank(message = "Не может быть пустым")
    private String name;
    @NotNull(message = "Введите email адрес")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Введите валидный email адрес")
    private String email;
}
