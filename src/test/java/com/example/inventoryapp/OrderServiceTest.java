package com.example.inventoryapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.inventoryapp.model.Item;
import com.example.inventoryapp.model.PurchaseOrder;
import com.example.inventoryapp.repository.OrderRepository;
import com.example.inventoryapp.service.ItemService;

@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetOrderById() {
        Long orderId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);

        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo(orderId);
        order.setItem(item);
        order.setQty(5);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Optional<PurchaseOrder> retrievedOrder = itemService.getOrderById(orderId);

        assertEquals(order.getOrderNo(), retrievedOrder.get().getOrderNo());
        assertEquals(order.getItem(), retrievedOrder.get().getItem());
        assertEquals(order.getQty(), retrievedOrder.get().getQty());
    }

    @Test
    public void testSaveOrder() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setItem(item);
        purchaseOrder.setQty(5);

        when(orderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

        PurchaseOrder savedOrder = itemService.saveOrder(purchaseOrder);

        assertEquals(purchaseOrder.getItem(), savedOrder.getItem());
        assertEquals(purchaseOrder.getQty(), savedOrder.getQty());
    }

    @Test
    public void testEditOrder() {
        Long orderId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);

        PurchaseOrder existingOrder = new PurchaseOrder();
        existingOrder.setOrderNo(orderId);
        existingOrder.setItem(item);
        existingOrder.setQty(5);

        PurchaseOrder newOrderData = new PurchaseOrder();
        newOrderData.setOrderNo(orderId);
        newOrderData.setItem(item);
        newOrderData.setQty(10);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(newOrderData)).thenReturn(newOrderData);

        PurchaseOrder updatedOrder = itemService.saveOrder(newOrderData);

        assertEquals(newOrderData.getQty(), updatedOrder.getQty());
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setPrice(10.0);

        PurchaseOrder orderToDelete = new PurchaseOrder();
        orderToDelete.setOrderNo(orderId);
        orderToDelete.setItem(item);
        orderToDelete.setQty(5);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderToDelete));

        itemService.deleteOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}
