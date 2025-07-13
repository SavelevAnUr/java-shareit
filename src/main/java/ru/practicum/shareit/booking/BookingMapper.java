package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default Booking toBooking(BookingDto dto, Item item, User booker, BookingStatus status) {
        if (dto == null) return null;

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setBooker(booker);
        booking.setStatus(status);
        return booking;
    }

    static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;

        BookingDto.ItemShortDto itemDto = new BookingDto.ItemShortDto(
                booking.getItem().getId(),
                booking.getItem().getName()
        );

        BookingDto.UserShortDto bookerDto = new BookingDto.UserShortDto(
                booking.getBooker().getId()
        );

        return new BookingDto(
                booking.getId(),
                itemDto,
                bookerDto,
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus()
        );
    }
}