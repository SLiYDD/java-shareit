package ru.practicum.shareit.item.web;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemDto createItem(ItemDto itemDto) {
        //itemService.create()
        return null;
    }
}
