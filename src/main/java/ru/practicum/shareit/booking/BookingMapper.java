package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Маппинг из DTO в Booking
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

    // Маппинг из Booking в DTO — теперь включает bookerId и itemName
    public static BookingDto toBookingDto(Booking booking) {
        if (booking == null) return null;

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setItemId(booking.getItem().getId());
        dto.setItemName(booking.getItem().getName());
        dto.setBookerId(booking.getBooker().getId());
        dto.setStart(booking.getStart());
        dto.setEnd(booking.getEnd());
        dto.setStatus(booking.getStatus());
        return dto;
    }
}