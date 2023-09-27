package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Item item, long ownerId);

    ItemDto updateItem(Item item, long ownerId);

    Item findItemOrThrow(Long itemId);

    ItemDto getItemById(long itemId);

    void deleteItem(long itemId);

    List<ItemDto> getAllItemsByOwnerId(long ownerId);

    List<ItemDto> getItemsBySearchQuery(String text);
}
