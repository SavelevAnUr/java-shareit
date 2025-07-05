package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default Booking toBooking(BookingDto dto, Long bookerId, BookingStatus status) {
        if (dto == null) return null;

        Booking booking = new Booking();
        booking.setItemId(dto.getItemId());
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setBookerId(bookerId);
        booking.setStatus(status);
        return booking;
    }

    default BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItemId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus()); // Если нужно
        return dto;
    }
}