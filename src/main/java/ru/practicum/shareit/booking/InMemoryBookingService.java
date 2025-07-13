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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
        if (bookingDto.getItemId() == null) {
            throw new IllegalArgumentException("Item id must be provided");
        }

        ItemDto itemDto = itemService.getItemById(bookingDto.getItemId());
        Long ownerId = itemDto.getOwnerId();
        Item item = ItemMapper.toItem(itemDto, ownerId);

        UserDto userDto = userService.getUserById(bookerId);
        User booker = UserMapper.toUser(userDto);

        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setId(idCounter.getAndIncrement());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);

        bookings.put(booking.getId(), booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long userId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) return null;

        boolean isBooker = booking.getBooker().getId().equals(userId);
        boolean isOwner = booking.getItem().getOwner().getId().equals(userId);

        if (!isBooker && !isOwner) {
            throw new IllegalArgumentException("Пользователь не имеет доступа к бронированию");
        }

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

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new IllegalArgumentException("Вы не являетесь владельцем предмета");
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUser(Long userId, String state, int from, int size) {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> filtered = bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(userId))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .toList();

        switch (state.toUpperCase()) {
            case "ALL":
                break;
            case "CURRENT":
                filtered = filtered.stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .toList();
                break;
            case "PAST":
                filtered = filtered.stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .toList();
                break;
            case "FUTURE":
                filtered = filtered.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .toList();
                break;
            case "WAITING":
                filtered = filtered.stream()
                        .filter(b -> b.getStatus() == BookingStatus.WAITING)
                        .toList();
                break;
            case "REJECTED":
                filtered = filtered.stream()
                        .filter(b -> b.getStatus() == BookingStatus.REJECTED)
                        .toList();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        int toIndex = Math.min(from + size, filtered.size());
        List<Booking> paginated = (from <= filtered.size()) ? filtered.subList(from, toIndex) : Collections.emptyList();

        return paginated.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long ownerId, String state, int from, int size) {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> filtered = bookings.values().stream()
                .filter(b -> b.getItem().getOwner().getId().equals(ownerId))
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .toList();

        switch (state.toUpperCase()) {
            case "ALL":
                break;
            case "CURRENT":
                filtered = filtered.stream()
                        .filter(b -> b.getStart().isBefore(now) && b.getEnd().isAfter(now))
                        .toList();
                break;
            case "PAST":
                filtered = filtered.stream()
                        .filter(b -> b.getEnd().isBefore(now))
                        .toList();
                break;
            case "FUTURE":
                filtered = filtered.stream()
                        .filter(b -> b.getStart().isAfter(now))
                        .toList();
                break;
            case "WAITING":
                filtered = filtered.stream()
                        .filter(b -> b.getStatus() == BookingStatus.WAITING)
                        .toList();
                break;
            case "REJECTED":
                filtered = filtered.stream()
                        .filter(b -> b.getStatus() == BookingStatus.REJECTED)
                        .toList();
                break;
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }

        int toIndex = Math.min(from + size, filtered.size());
        List<Booking> paginated = (from <= filtered.size()) ? filtered.subList(from, toIndex) : Collections.emptyList();

        return paginated.stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}