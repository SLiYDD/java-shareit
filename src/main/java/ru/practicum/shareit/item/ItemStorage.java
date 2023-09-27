package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item createItem(Item item);

    Item updateItem(Item item);

    Optional<Item> findItemById(Long itemId);

    List<ItemDto> findAllItemsByOwnerId(long ownerId);

    List<Item> findItemsByQuery(String text);

    void removeItem(long itemId);
}
