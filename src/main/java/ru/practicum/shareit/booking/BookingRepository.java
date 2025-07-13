package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Все бронирования пользователя, сортировка по start desc с пагинацией
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    // Текущие бронирования пользователя (start < now < end)
    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,
                                                                          LocalDateTime nowStart,
                                                                          LocalDateTime nowEnd,
                                                                          Pageable pageable);

    // Прошедшие бронирования пользователя (end < now)
    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId,
                                                             LocalDateTime now,
                                                             Pageable pageable);

    // Будущие бронирования пользователя (start > now)
    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId,
                                                              LocalDateTime now,
                                                              Pageable pageable);

    // Бронирования пользователя по статусу
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId,
                                                          BookingStatus status,
                                                          Pageable pageable);

    // Аналогичные методы для владельца предметов

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId,
                                                                             LocalDateTime nowStart,
                                                                             LocalDateTime nowEnd,
                                                                             Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId,
                                                                LocalDateTime now,
                                                                Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId,
                                                                 LocalDateTime now,
                                                                 Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId,
                                                             BookingStatus status,
                                                             Pageable pageable);
    List<Booking> findByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime end);
}
