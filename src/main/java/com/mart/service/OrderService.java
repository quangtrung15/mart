package com.mart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mart.dto.OrderDTO;
import com.mart.entity.OrderDetail;

@Service
public interface OrderService {

	boolean createOrder(int userId, String address, String createdDate, List<OrderDetail> orderDetails, String payment,
			String phone);

	boolean updateOrder(int orderId, int userId, String updatedDate, String address, List<OrderDetail> orderDetails,
			String payment, String phone);

	boolean deleteOrder(int orderId);

	List<OrderDTO> getOrderByUserId(int userId);

	boolean changeOrderStatus(int userId, int orderId, String status);

}
