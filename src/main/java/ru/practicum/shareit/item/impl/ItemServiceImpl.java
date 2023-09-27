package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;
import ru.practicum.shareit.user.UserService;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto createItem(Item item, long ownerId) {
        userService.findUserOrThrow(ownerId);
        return Mapper.toItemDto(itemStorage.createItem(item.toBuilder().ownerId(ownerId).build()));
    }

    @Override
    public ItemDto updateItem(@Valid Item item, long ownerId) {
        userService.findUserOrThrow(ownerId);
        var updatableItem = findItemOrThrow(item.getId());
        if (updatableItem.getOwnerId() == (ownerId)) {
            return Mapper.toItemDto(itemStorage.updateItem(item.toBuilder().ownerId(ownerId).build()));
        }
        throw new NotFoundException("Этой вещи у пользователя с ID = " + ownerId + " не найдено");
    }

    @Override
    public Item findItemOrThrow(Long itemId) {
        return itemStorage.findItemById(itemId).orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена"));
    }


    @Override
    public ItemDto getItemById(long itemId) {
        return Mapper.toItemDto(itemStorage.findItemById(itemId).orElseThrow(() -> new NotFoundException("Вещь с ID = " + itemId + " не найдена")));
    }

    @Override
    public void deleteItem(long itemId) {
        findItemOrThrow(itemId);
        itemStorage.removeItem(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsByOwnerId(long ownerId) {
        userService.findUserOrThrow(ownerId);
        return itemStorage.findAllItemsByOwnerId(ownerId);
    }

    @Override
    public List<ItemDto> getItemsBySearchQuery(String text) {
        return itemStorage.findItemsByQuery(text.toLowerCase()).stream()
                .map(Mapper::toItemDto)
                .collect(Collectors.toList());
    }
}
