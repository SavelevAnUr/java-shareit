package ru.practicum.shareit.booking;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InMemoryBookingService implements BookingService {

    private final Map<Long, Booking> bookings = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final BookingMapper bookingMapper;
    private final ItemService itemService;
    private final UserService userService;

    public InMemoryBookingService(BookingMapper bookingMapper,
                                  ItemService itemService,
                                  UserService userService) {
        this.bookingMapper = bookingMapper;
        this.itemService = itemService;
        this.userService = userService;
    }

    @Override
    public BookingDto createBooking(Long bookerId, BookingDto bookingDto) {
        ItemDto itemDto = itemService.getItemById(bookingDto.getItemId());
        Long ownerId = itemDto.getOwnerId();
        Item item = ItemMapper.toItem(itemDto, ownerId);
        UserDto userDto = userService.getUserById(bookerId);
        User booker = UserMapper.toUser(userDto);

        Booking booking = bookingMapper.toBooking(bookingDto, item, booker, BookingStatus.WAITING);
        booking.setId(idCounter.getAndIncrement());
        bookings.put(booking.getId(), booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return null;
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllBookingsByBooker(Long bookerId) {
        return bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(bookerId))
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllBookingsByOwner(Long ownerId) {
        return bookings.values().stream()
                .filter(b -> b.getItem().getOwner().getId().equals(ownerId))
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, Boolean approved) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return null;

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUser(Long userId, String state, int from, int size) {
        // Пример простой реализации — фильтрация по bookerId, без учёта state и пагинации
        return bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(userId))
                .skip(from)
                .limit(size)
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long ownerId, String state, int from, int size) {
        // Аналогично — фильтрация по ownerId (в вашем случае, возможно, itemId — проверьте логику)
        return bookings.values().stream()
                .filter(b -> b.getItem().getOwner().getId().equals(ownerId))
                .skip(from)
                .limit(size)
                .map(BookingMapper::toBookingDto)
                .toList();
    }
}