package com.example.shopapp.services.order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.shopapp.dtos.OrderDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.responses.order.OrderResponse;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    
    Order getOrderById(Long orderId);
    
    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    
    void deleteOrder(Long orderId);
    
    List<OrderResponse> findByUserId(Long userId);
    
    Page<Order> getOrdersByKeyword(String keyword, Pageable pageable);
}
