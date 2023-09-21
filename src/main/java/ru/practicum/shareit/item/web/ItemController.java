package ru.practicum.shareit.item.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.mapper.Mapper;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER) long ownerId) {
        itemService.createItem(Mapper.toItem(itemDto), ownerId);
        return null;
    }
}
