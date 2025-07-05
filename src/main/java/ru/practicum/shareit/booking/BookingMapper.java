package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "dto.itemId", target = "itemId")
    @Mapping(source = "dto.start", target = "start")
    @Mapping(source = "dto.end", target = "end")
    @Mapping(target = "bookerId", source = "bookerId")
    @Mapping(target = "status", source = "status")
    Booking toBooking(BookingDto dto, Long bookerId, BookingStatus status);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "start", target = "start")
    @Mapping(source = "end", target = "end")
    @Mapping(source = "status", target = "status")
    BookingDto toBookingDto(Booking booking);
}