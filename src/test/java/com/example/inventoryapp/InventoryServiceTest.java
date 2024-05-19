package com.example.inventoryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.inventoryapp.model.Inventory;
import com.example.inventoryapp.repository.InventoryRepository;
import com.example.inventoryapp.service.ItemService;

@SpringBootTest
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetInventoryById() {
        Long inventoryId = 1L;
        Inventory inventory = new Inventory();
        inventory.setId(inventoryId);
        inventory.setQty(10);
        inventory.setType("T");

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventory));

        Optional<Inventory> retrievedInventory = itemService.getInventoryById(inventoryId);

        assertEquals(inventory.getId(), retrievedInventory.get().getId());
        assertEquals(inventory.getQty(), retrievedInventory.get().getQty());
        assertEquals(inventory.getType(), retrievedInventory.get().getType());
    }

    @Test
    public void testSaveInventory() {
        Inventory inventory = new Inventory();
        inventory.setQty(10);
        inventory.setType("T");

        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        Inventory savedInventory = itemService.saveInventory(inventory);

        assertEquals(inventory.getQty(), savedInventory.getQty());
        assertEquals(inventory.getType(), savedInventory.getType());
    }

    @Test
    public void testEditInventory() {
        Long inventoryId = 1L;
        Inventory existingInventory = new Inventory();
        existingInventory.setId(inventoryId);
        existingInventory.setQty(10);
        existingInventory.setType("T");

        Inventory newInventoryData = new Inventory();
        newInventoryData.setId(inventoryId);
        newInventoryData.setQty(20);
        newInventoryData.setType("W");

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepository.save(newInventoryData)).thenReturn(newInventoryData);

        Inventory updatedInventory = itemService.saveInventory(newInventoryData);

        assertEquals(newInventoryData.getQty(), updatedInventory.getQty());
        assertEquals(newInventoryData.getType(), updatedInventory.getType());
    }

    @Test
    public void testDeleteInventory() {
        Long inventoryId = 1L;
        Inventory inventoryToDelete = new Inventory();
        inventoryToDelete.setId(inventoryId);
        inventoryToDelete.setQty(10);
        inventoryToDelete.setType("T");

        when(inventoryRepository.findById(inventoryId)).thenReturn(Optional.of(inventoryToDelete));

        itemService.deleteInventory(inventoryId);

        verify(inventoryRepository, times(1)).deleteById(inventoryId);
    }
}
