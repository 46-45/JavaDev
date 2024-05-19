package com.example.inventoryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.repository.ItemRepository;
import com.example.inventoryapp.service.ItemService;

@SpringBootTest
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetItemById() {
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setName("Test Item");
        item.setPrice(10.0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Optional<Item> retrievedItem = itemService.getItemById(itemId);

        assertEquals(item.getId(), retrievedItem.get().getId());
        assertEquals(item.getName(), retrievedItem.get().getName());
        assertEquals(item.getPrice(), retrievedItem.get().getPrice());
    }

    @Test
    public void testSaveItem() {
        Item item = new Item();
        item.setName("Test Item");
        item.setPrice(10.0);

        when(itemRepository.save(item)).thenReturn(item);

        Item savedItem = itemService.saveItem(item);

        assertEquals(item.getName(), savedItem.getName());
        assertEquals(item.getPrice(), savedItem.getPrice());
    }

    @Test
    public void testEditItem() {
        Long itemId = 1L;
        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Existing Item");
        existingItem.setPrice(20.0);

        Item newItemData = new Item();
        newItemData.setId(itemId);
        newItemData.setName("Updated Item");
        newItemData.setPrice(30.0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(newItemData)).thenReturn(newItemData);

        Item updatedItem = itemService.saveItem(newItemData);

        assertEquals(newItemData.getName(), updatedItem.getName());
        assertEquals(newItemData.getPrice(), updatedItem.getPrice());
    }

    @Test
    public void testDeleteItem() {
        Long itemId = 1L;
        Item itemToDelete = new Item();
        itemToDelete.setId(itemId);
        itemToDelete.setName("Item to Delete");
        itemToDelete.setPrice(15.0);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemToDelete));

        itemService.deleteItem(itemId);

        verify(itemRepository, times(1)).deleteById(itemId);
    }
}
