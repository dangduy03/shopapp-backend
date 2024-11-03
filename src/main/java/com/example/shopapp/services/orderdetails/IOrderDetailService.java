package com.example.shopapp.services.orderdetails;

import java.util.List;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.OrderDetail;

public interface IOrderDetailService {
	
	OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception ;
	
	OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
	
	OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException ;
	
	void deleteById(Long id);
	
	List<OrderDetail> findByOrderId(Long orderId);
	
	
 
}
