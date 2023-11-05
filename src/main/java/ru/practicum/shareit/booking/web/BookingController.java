package ru.practicum.shareit.booking.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
public class BookingController {
    private static final String USER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingInputDto inputDto, @RequestHeader(USER) long ownerId) {
        log.info("Получен POST-запрос: '/bookings' " +
                "на создание бронирования от пользователя с ID={}", ownerId);
        return bookingService.createBooking(inputDto, ownerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable("bookingId") long bookingId, @RequestHeader(USER) long ownerId, @RequestParam Boolean approved) {
        log.info("Получен PATCH-запрос: '/bookings' на обновление статуса бронирования с ID={}", bookingId);
        return bookingService.update(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable("bookingId") long bookingId, @RequestHeader(USER) long ownerId) {
        log.info("Получен GET-запрос: '/bookings' на получение бронирования с ID={}", bookingId);
        return bookingService.findBookingById(bookingId, ownerId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByUserId(@RequestHeader(USER) long userId, @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Получен GET-запрос: '/bookings' на получение " +
                "списка всех бронирований пользователя с ID={} с параметром STATE={}", userId, state);
        return bookingService.findAllBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingByOwnerId(@RequestHeader(USER) long ownerId, @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.info("Получен GET-запрос: '/bookings/owner' на получение " +
                "списка всех бронирований вещей пользователя с ID={} с параметром STATE={}", ownerId, state);
        return bookingService.findAllBookingsByOwnerId(ownerId, state);
    }
}
