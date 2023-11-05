package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.model.Booking;


import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingInputDto inputDto, long userId);

    BookingDto update(long bookingId, long ownerId, Boolean approved);

    Booking findBookingOrThrow(long bookingId);

    BookingDto findBookingById(long bookingId, long userId);

    List<BookingDto> findAllBookings(long userId, String state);

    List<BookingDto> findAllBookingsByOwnerId(long ownerId, String state);

}
