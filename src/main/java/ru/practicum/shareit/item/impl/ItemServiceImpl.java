package ru.practicum.shareit.item.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Override
    public ItemDto create(ItemDto itemDto) {
        return itemStorage.createItem(Mapper.toItem(itemDto));
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return null;
    }

    @Override
    public void delete(long itemId) {

    }
}
