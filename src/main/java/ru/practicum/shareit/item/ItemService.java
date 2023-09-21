package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {

    ItemDto createItem(Item item, long ownerId);

    ItemDto updateItem(ItemDto itemDto);

    ItemDto getItemById(long itemId);

    void deleteItem(long itemId);
}
