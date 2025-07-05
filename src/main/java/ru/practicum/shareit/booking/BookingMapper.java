package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "status", source = "status")
    Booking toBooking(BookingDto dto, Long bookerId, BookingStatus status);

    default Booking toBooking(BookingDto dto, Long bookerId, String statusString) {
        return toBooking(dto, bookerId, BookingStatus.valueOf(statusString));
    }

    // Dto mapping
    @Mapping(source = "booking.id", target = "id")
    @Mapping(source = "booking.itemId", target = "itemId")
    @Mapping(source = "booking.start", target = "start")
    @Mapping(source = "booking.end", target = "end")
    BookingDto toBookingDto(Booking booking);
}