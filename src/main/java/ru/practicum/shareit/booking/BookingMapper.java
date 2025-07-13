package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Преобразование из Booking в BookingDto
    static BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        UserDto userDto = UserMapper.toUserDto(booking.getBooker());

        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem() != null ? booking.getItem().getId() : null,
                userDto,
                booking.getStatus().name()
        );
    }

    // Преобразование из BookingDto в Booking
    static Booking toBooking(BookingDto bookingDto, Item item, User booker, BookingStatus status) {
        if (bookingDto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);

        return booking;
    }
}
