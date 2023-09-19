package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

    ItemDto create(ItemDto itemDto);

    ItemDto update(ItemDto itemDto);

    ItemDto getItemById(long itemId);

    void delete(long itemId);
}
