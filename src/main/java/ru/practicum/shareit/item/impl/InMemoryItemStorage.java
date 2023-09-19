package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;

import java.util.HashMap;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Long, Item> items = new HashMap<>();
    private static long countId = 0;
    @Override
    public ItemDto createItem(Item item) {
        item.setId(++countId);
        items.put(item.getId(), item);
        return Mapper.toItemDto(item);
    }
}
