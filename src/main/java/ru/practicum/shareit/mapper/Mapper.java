package ru.practicum.shareit.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Component
@RequiredArgsConstructor
public final class Mapper {

    public static ItemDto toItemDto(Item item, List<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .comments(comments)
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto toItemOwnerDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDto.BookingShortDto toBookingShortDto(Booking booking) {
        if (booking == null) {
            throw new NotFoundException("Бронирование не найдено!");
        }
        return ItemDto.BookingShortDto.builder()
                .id(booking.getId())
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .bookerId(booking.getBooker().getId())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .booker(booking.getBooker())
                .item(toItemShortDto(booking.getItem()))
                .build();
    }

    private static BookingDto.ItemShortDto toItemShortDto(Item item) {
        return BookingDto.ItemShortDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }

    public static Booking toBooking(BookingInputDto bookingInputDto) {
        return Booking.builder()
                .start(bookingInputDto.getStart())
                .end(bookingInputDto.getEnd())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .created(comment.getCreated())
                .text(comment.getText())
                .item(comment.getItem())
                .authorName(comment.getAuthor().getName())
                .build();
    }
}
