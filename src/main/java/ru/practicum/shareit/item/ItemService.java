package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Item item, long ownerId);

    ItemDto updateItem(Item item, long ownerId);

    Item findItemOrThrow(Long itemId);

    ItemDto getItemById(long itemId, Long ownerId);

    void deleteItem(long itemId);

    List<ItemDto> getAllItemsByOwnerId(long ownerId);

    List<ItemDto> getItemsBySearchQuery(String text);

    CommentDto createComment(CommentDto commentDto, Long itemId, Long userId);

    List<CommentDto> getCommentsByItemId(Long itemId);
}
