package ru.practicum.shareit.item.web;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.mapper.Mapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private static final String OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER) long ownerId) {
        return itemService.createItem(Mapper.toItem(itemDto), ownerId);
    }


    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) long ownerId, @PathVariable long itemId) {
        ItemDto oldItem = itemService.getItemById(itemId);

        if (Objects.isNull(itemDto.getName())) {
            itemDto.setName(oldItem.getName());
        }

        if (Objects.isNull(itemDto.getDescription())) {
            itemDto.setDescription(oldItem.getDescription());
        }

        if (Objects.isNull(itemDto.getAvailable())) {
            itemDto.setAvailable(oldItem.getAvailable());
        }

        if (Objects.isNull(itemDto.getRequestId())) {
            itemDto.setRequestId(oldItem.getRequestId());
        }

        return itemService.updateItem(Mapper.toItem(itemDto.toBuilder().id(itemId).build()), ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(OWNER) long ownerId) {
        return itemService.getAllItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestParam String text) {
        return itemService.getItemsBySearchQuery(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId) {
        itemService.deleteItem(itemId);
    }

}
