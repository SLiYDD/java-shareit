package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {
    private HashMap<Long, Item> items = new HashMap<>();
    private static long countId = 0;

    @Override
    public Item createItem(Item item) {
        item.setId(++countId);
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        if (items.containsKey(itemId)) {
            return Optional.of(items.get(itemId));
        }
        return Optional.empty();
    }

    @Override
    public List<ItemDto> findAllItemsByOwnerId(long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == ownerId)
                .map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findItemsByQuery(String text) {
        if (!text.isBlank()) {
            return items.values().stream()
                    .filter(Item::getAvailable)
                    .filter(item -> item.getName().toLowerCase().equals(text) || item.getDescription().toLowerCase().contains(text))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void removeItem(long itemId) {
        items.remove(itemId);
    }
}
