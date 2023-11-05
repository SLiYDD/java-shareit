package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.item.CommentRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;


import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;


    @Override
    public ItemDto createItem(Item item, long ownerId) {
        User owner = userService.findUserOrThrow(ownerId);
        return Mapper.toItemDto(itemRepository.save(item.toBuilder().owner(owner).build()), getCommentsByItemId(item.getId()));
    }


    @Transactional
    @Override
    public ItemDto updateItem(@Valid Item item, long ownerId) {
        User findUser = userService.findUserOrThrow(ownerId);
        var updatableItem = findItemOrThrow(item.getId());
        if (updatableItem.getOwner().equals(findUser)) {
            return Mapper.toItemDto(itemRepository.save((item.toBuilder().owner(findUser).build())), getCommentsByItemId(item.getId()));
        }
        throw new NotFoundException("Этой вещи у пользователя с ID = " + ownerId + " не найдено");
    }


    @Override
    public Item findItemOrThrow(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена"));
    }


    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(long itemId, Long ownerId) {
        userService.findUserOrThrow(ownerId);
        Item item = findItemOrThrow(itemId);
        ItemDto itemDto;
        List<Booking> bookings = bookingRepository.findByItemIdAndStatus(itemId, Status.APPROVED, Sort.by(Sort.Direction.ASC, "start"));
        List<ItemDto.BookingShortDto> bookingDtoShorts = bookings.stream()
                .map(Mapper::toBookingShortDto)
                .collect(Collectors.toList());
        if (ownerId.equals(item.getOwner().getId())) {
            itemDto = Mapper.toItemOwnerDto(item);
            setBookings(itemDto, bookingDtoShorts);
            itemDto.setComments(getCommentsByItemId(itemId));
        } else {
            itemDto = Mapper.toItemDto(item, getCommentsByItemId(itemId));
        }
        return itemDto;
    }


    private void setBookings(ItemDto itemDto, List<ItemDto.BookingShortDto> bookings) {
        if (!bookings.isEmpty()) {
            itemDto.setLastBooking(bookings.stream()
                    .filter(booking -> booking.getItemId().equals(itemDto.getId()) &&
                            (booking.getStart().isBefore(LocalDateTime.now()) || booking.getEnd().isBefore(LocalDateTime.now())))
                    .reduce((bookRes, bookPrev) -> bookPrev).orElse(null));
            itemDto.setNextBooking(bookings.stream()
                    .filter(booking -> booking.getItemId().equals(itemDto.getId()) && booking.getStart().isAfter(LocalDateTime.now()))
                    .reduce((bookNext, bookRes) -> bookNext).orElse(null));
        }

    }


    @Override
    public void deleteItem(long itemId) {
        findItemOrThrow(itemId);
        itemRepository.deleteById(itemId);
    }


    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getAllItemsByOwnerId(long ownerId) {
        List<ItemDto> itemsDto = itemRepository.findByOwnerId(ownerId).stream()
                .map(Mapper::toItemOwnerDto)
                .sorted(Comparator.comparing(ItemDto::getId))
                .collect(Collectors.toList());
        List<Booking> bookings = bookingRepository.findAllByOwnerId(ownerId, Status.APPROVED,
                Sort.by(Sort.Direction.ASC, "start"));
        List<ItemDto.BookingShortDto> bookingDtoShorts = bookings.stream()
                .map(Mapper::toBookingShortDto)
                .collect(Collectors.toList());
        itemsDto.forEach(itemDto -> setBookings(itemDto, bookingDtoShorts));
        return itemsDto;
    }


    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemsBySearchQuery(String text) {
        if (!text.isBlank()) {
            return itemRepository.findItemBySearchQuery(text).stream()
                    .map(item -> Mapper.toItemDto(item, getCommentsByItemId(item.getId())))
                    .collect(toList());
        }
        return new ArrayList<>();
    }


    @Transactional
    @Override
    public CommentDto createComment(CommentDto commentDto, Long itemId, Long userId) {
        userService.findUserOrThrow(userId);
        Booking booking = bookingRepository.findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(itemId, userId, LocalDateTime.now(), Status.APPROVED);
        if (Objects.isNull(booking)) {
            throw new NotValidException("Данный пользователь вещь не бронировал!");
        }
        return Mapper.toCommentDto(commentRepository.save(Comment.builder()
                .created(LocalDateTime.now())
                .item(booking.getItem())
                .author(booking.getBooker())
                .text(commentDto.getText())
                .build()));
    }


    @Override
    public List<CommentDto> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItemId(itemId, Sort.by(Sort.Direction.DESC, "created")).stream()
                .map(Mapper::toCommentDto)
                .collect(toList());
    }
}
