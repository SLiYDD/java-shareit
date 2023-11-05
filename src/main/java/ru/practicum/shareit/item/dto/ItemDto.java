package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @NotBlank(message = "Описание не может быть пустым")
    private String description;
    @NotNull(message = "Поле available не может быть пустым")
    private Boolean available;
    private Long requestId;
    private BookingShortDto lastBooking;
    private BookingShortDto nextBooking;
    private List<CommentDto> comments;


    @Builder(toBuilder = true)
    @Getter
    public static class BookingShortDto {
        private Long id;
        private Long itemId;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long bookerId;
    }
}
