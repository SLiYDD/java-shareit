package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long bookerId, Sort sort);

    List<Booking> findAllByItemOwnerId(long ownerId, Sort sort);

    @Query(value = "select b from Booking b where b.item.owner.id = ?1 and b.status = ?2")
    List<Booking> findAllByOwnerId(long ownerId, Status status, Sort sort);

    List<Booking> findByItemIdAndStatus(long itemId, Status status, Sort sort);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsBeforeAndEndIsAfter(Long ownerId, LocalDateTime start, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndStatus(Long bookerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStatus(Long ownerId, Status status, Sort sort);

    List<Booking> findAllByBookerIdAndStartIsAfter(long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findAllByItemOwnerIdAndStartIsAfter(long ownerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(long bookerId, LocalDateTime currentTime, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(long ownerId, LocalDateTime currentTime, Sort sort);

    Booking findFirstByItemIdAndBookerIdAndEndIsBeforeAndStatus(Long itemId, Long userId, LocalDateTime end, Status status);
}
