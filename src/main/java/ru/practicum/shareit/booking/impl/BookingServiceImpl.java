package ru.practicum.shareit.booking.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.NotValidException;
import ru.practicum.shareit.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto createBooking(BookingInputDto inputDto, long bookerId) {
        if (inputDto.getStart().isAfter(inputDto.getEnd()) || inputDto.getStart().equals(inputDto.getEnd())) {
            throw new NotValidException("Дата старта не может быть после или равна дате окончания");
        }
        User user = userService.findUserOrThrow(bookerId);
        Item item = itemService.findItemOrThrow(inputDto.getItemId());
        if (bookerId == item.getOwner().getId()) {
            throw new NotFoundException("Недоступно для бронирования самим владельцем");
        }
        Booking booking = Mapper.toBooking(inputDto).toBuilder()
                .booker(user)
                .item(item)
                .status(Status.WAITING)
                .build();
        if (!item.getAvailable()) {
            throw new NotValidException("Вещь недоступна для бронирования");
        }
        return Mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto update(long bookingId, long ownerId, Boolean approved) {
        Booking booking = findBookingOrThrow(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new NotFoundException("Вносить изменения может только владелец вещи");
        }
        if (!Status.WAITING.equals(booking.getStatus())) {
            throw new NotValidException("Решение уже принято!");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return Mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public Booking findBookingOrThrow(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Букинг не найден"));
    }

    @Override
    public BookingDto findBookingById(long bookingId, long userId) {
        userService.findUserOrThrow(userId);
        Booking booking = findBookingOrThrow(bookingId);
        if (booking.getItem().getOwner().getId() != userId && booking.getBooker().getId() != userId) {
            throw new NotFoundException("Просматривать данные может только владелец вещи");
        }
        return Mapper.toBookingDto(findBookingOrThrow(bookingId));
    }

    @Override
    public List<BookingDto> findAllBookings(long userId, String state) {
        userService.findUserOrThrow(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByBookerId(userId, sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfter(userId, LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(), sort);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(Mapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findAllBookingsByOwnerId(long ownerId, String state) {
        userService.findUserOrThrow(ownerId);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        final List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findAllByItemOwnerId(ownerId, sort);
                break;
            case "FUTURE":
                bookings = bookingRepository.findAllByItemOwnerIdAndStartIsAfter(ownerId, LocalDateTime.now(), sort);
                break;
            case "WAITING":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, Status.WAITING, sort);
                break;
            case "REJECTED":
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, Status.REJECTED, sort);
                break;
            case "CURRENT":
                bookings = bookingRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(ownerId, LocalDateTime.now(), LocalDateTime.now(), sort);
                break;
            case "PAST":
                bookings = bookingRepository.findByItemOwnerIdAndEndIsBefore(ownerId, LocalDateTime.now(), sort);
                break;
            default:
                throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(Mapper::toBookingDto).collect(Collectors.toList());
    }
}
