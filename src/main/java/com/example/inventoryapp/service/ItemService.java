package com.example.inventoryapp.service;

import com.example.inventoryapp.exception.ResourceNotFoundException;
import com.example.inventoryapp.model.Inventory;
import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.model.PurchaseOrder;
import com.example.inventoryapp.repository.InventoryRepository;
import com.example.inventoryapp.repository.ItemRepository;
import com.example.inventoryapp.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public Integer getRemainingStock(Long itemId) {
        List<Inventory> inventoryList = inventoryRepository.findByItemId(itemId);
        int totalIn = inventoryList.stream().filter(inv -> "T".equals(inv.getType())).mapToInt(Inventory::getQty).sum();
        int totalOut = inventoryList.stream().filter(inv -> "W".equals(inv.getType())).mapToInt(Inventory::getQty).sum();
        return totalIn - totalOut;
    }

    @Transactional
    public PurchaseOrder saveOrder(PurchaseOrder order) {
        Integer remainingStock = getRemainingStock(order.getItem().getId());
        if (remainingStock < order.getQty()) {
            throw new ResourceNotFoundException("Insufficient stock for item ID: " + order.getItem().getId());
        }

        Inventory withdrawal = new Inventory();
        withdrawal.setItem(order.getItem());
        withdrawal.setQty(order.getQty());
        withdrawal.setType("W");
        inventoryRepository.save(withdrawal);
        return orderRepository.save(order);
    }

    public Inventory saveInventory(Inventory inventory) {
        if (!"T".equals(inventory.getType()) && !"W".equals(inventory.getType())) {
            throw new IllegalArgumentException("Invalid inventory type");
        }
        return inventoryRepository.save(inventory);
    }

    public Page<Inventory> getAllInventories(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    public Page<PurchaseOrder> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<PurchaseOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
