package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    @Query("select i from Item i " +
            "where lower(i.name) like lower(concat('%', :query, '%') ) " +
            "or lower(i.description) like lower(concat('%', :query, '%')) " +
            "and i.available = true")
    List<Item> findItemBySearchQuery(@Param("query") String text);
}
