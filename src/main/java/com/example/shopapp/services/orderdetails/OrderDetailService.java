package com.example.shopapp.services.orderdetails;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopapp.dtos.OrderDetailDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Order;
import com.example.shopapp.models.OrderDetail;
import com.example.shopapp.models.Product;
import com.example.shopapp.repositorys.OrderDetailRepository;
import com.example.shopapp.repositorys.OrderRepository;
import com.example.shopapp.repositorys.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderDetailService implements IOrderDetailService{
	
	private final OrderRepository orderRepository;
	
	private final OrderDetailRepository orderDetailRepository;
	
	private final ProductRepository productRepository;

	@Override
	@Transactional
	public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {

		Order order = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(() -> new DataNotFoundException(
						"Cannot find Order with id : " + orderDetailDTO.getOrderId())); 

		Product product = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(() -> new DataNotFoundException(
						"Cannot find Product with id : " + orderDetailDTO.getProductId())); 
		
		OrderDetail orderDetail = OrderDetail.builder()
				.order(order)
				.product(product)
				.numberOfProducts(orderDetailDTO.getNumberOfProducts())
				.price(orderDetailDTO.getPrice())
				.totalMoney(orderDetailDTO.getTotalMoney())
				.color(orderDetailDTO.getColor())
				.build();

		return orderDetailRepository.save(orderDetail);
	}

	@Override
	public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
		return orderDetailRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException("Cannot find OrderDetail with id " +id));
	}

	@Override
	@Transactional
	public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) 
			throws DataNotFoundException {

		OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
				.orElseThrow(() -> new DataNotFoundException(
						"Cannot find order detail with id : "+id));
		
		Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
				.orElseThrow(() -> new DataNotFoundException(
						"Cannot find order with id: "+id));
		
		Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
				.orElseThrow(() -> new DataNotFoundException(
						"Cannot find Product with id : " + orderDetailDTO.getProductId())); 
		
		existingOrderDetail.setPrice(orderDetailDTO.getPrice());
		existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
		existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
		existingOrderDetail.setColor(orderDetailDTO.getColor());
		existingOrderDetail.setOrder(existingOrder);
		existingOrderDetail.setProduct(existingProduct);
		return orderDetailRepository.save(existingOrderDetail);
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		orderDetailRepository.deleteById(id);
		
	}

	@Override
	public List<OrderDetail> findByOrderId(Long orderId) {
		return orderDetailRepository.findByOrderId(orderId);
	}

}
