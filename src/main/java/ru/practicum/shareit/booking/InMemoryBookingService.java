package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryBookingService implements BookingService {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    private final BookingMapper bookingMapper;

    public InMemoryBookingService(BookingMapper bookingMapper) {
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto createBooking(Long bookerId, BookingDto bookingDto) {
        Booking booking = bookingMapper.toBooking(bookingDto);
        booking.setId(idCounter.getAndIncrement());
        booking.setBookerId(bookerId);
        booking.setStatus(BookingStatus.WAITING);

        bookings.put(booking.getId(), booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking with id=" + bookingId + " not found");
        }
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBooker(Long bookerId) {
        return bookings.values().stream()
                .filter(b -> b.getBookerId().equals(bookerId))
                .map(booking -> bookingMapper.toBookingDto(booking))
                .toList();
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId) {
        return bookings.values().stream()
                .filter(b -> b.getItemId().equals(ownerId))
                .map(booking -> bookingMapper.toBookingDto(booking))
                .toList();
    }

    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new NotFoundException("Booking with id=" + bookingId + " not found");
        }

        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new IllegalArgumentException("Only WAITING bookings can be approved or rejected.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingMapper.toBookingDto(booking);
    }
}