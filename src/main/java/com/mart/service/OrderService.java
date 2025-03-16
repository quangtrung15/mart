package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.OrderDTO;
import com.mart.entity.Order;
import com.mart.entity.OrderDetail;

@Service
public interface OrderService {

	// Create Order
	OrderDTO createOrder(long userId, String address, List<OrderDetail> orderDetails, String payment, String phone);

	// Update Order
	OrderDTO updateOrder(long orderId, long userId, String updatedDate, String address, List<OrderDetail> orderDetails,
			String payment, String phone);

	// Delete Order
	boolean deleteOrder(long orderId);

	// Display order list by user
	List<OrderDTO> getOrderByUserId(long userId);

	OrderDTO changeOrderStatus(long userId, long orderId, String status);

	String orderPayment(long orderId);
	
	void updateOrderPaymentStatus(String orderId, String status);

}
