package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private ItemShortDto item;
    private UserShortDto booker;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemShortDto {
        private Long id;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserShortDto {
        private Long id;
    }
}