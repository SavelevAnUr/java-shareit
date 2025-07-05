package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Маппинг из BookingDto + дополнительных параметров в Booking
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "end", target = "end")
    @Mapping(source = "bookerId", target = "bookerId")
    @Mapping(source = "status", target = "status")
    Booking toBooking(BookingDto dto, Long bookerId, BookingStatus status);

    // Маппинг из Booking в BookingDto
    @Mapping(source = "id", target = "id")
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "end", target = "end")
    @Mapping(source = "status", target = "status")
    BookingDto toBookingDto(Booking booking);
}