package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;

@Mapper
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    // Маппинг из DTO в сущность, игнорируем статус и bookerId, т.к. они добавляются отдельно
    @Mapping(target = "bookerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Booking toBooking(BookingDto dto);

    // Маппинг из сущности в DTO
    BookingDto toBookingDto(Booking booking);
}