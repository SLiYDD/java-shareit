package ru.practicum.shareit.item.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.Mapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private static final String OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(OWNER) long ownerId) {
        log.info("Получен POST-запрос: '/items' на добавление вещи владельцем с ID={}", ownerId);
        return itemService.createItem(Mapper.toItem(itemDto), ownerId);
    }


    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @RequestHeader(OWNER) long ownerId, @PathVariable long itemId) {
        log.info("Получен PATCH-запрос: '/items' на обновление вещи с ID={}", itemId);
        Item oldItem = itemService.findItemOrThrow(itemId);

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
    public ItemDto getItemById(@PathVariable long itemId, @RequestHeader(OWNER) Long ownerId) {
        log.info("Получен GET-запрос: '/items' на получение вещи с ID={}", itemId);
        return itemService.getItemById(itemId, ownerId);
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader(OWNER) long ownerId) {
        log.info("Получен GET-запрос: '/items' на получение всех вещей владельца с ID={}", ownerId);
        return itemService.getAllItemsByOwnerId(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> itemSearch(@RequestParam String text) {
        log.info("Получен GET-запрос: '/items/search' на поиск вещи с текстом={}", text);
        return itemService.getItemsBySearchQuery(text);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable long itemId) {
        log.info("Получен DELETE-запрос: '/items' на удаление вещи с ID={}", itemId);
        itemService.deleteItem(itemId);
    }

    @ResponseBody
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader(OWNER) Long userId,
                                    @PathVariable Long itemId) {
        log.info("Получен POST-запрос: '/items/comment' на" +
                " добавление отзыва пользователем с ID={}", userId);
        return itemService.createComment(commentDto, itemId, userId);
    }

}
