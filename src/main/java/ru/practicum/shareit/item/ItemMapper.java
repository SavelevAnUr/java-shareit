package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

public class ItemMapper {
    public static Item toItem(ItemDto dto, Long ownerId) {
        User owner = new User();
        owner.setId(ownerId);
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), owner, null);
    }

    public static ItemDto toItemDto(Item item) {
        Long ownerId = (item.getOwner() != null) ? item.getOwner().getId() : null;
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(), ownerId);
    }
}