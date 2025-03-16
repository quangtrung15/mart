package com.mart.service;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mart.config.Config;
import com.mart.dto.OrderDTO;
import com.mart.entity.KeyOrderDetail;
import com.mart.entity.Order;
import com.mart.entity.OrderDetail;
import com.mart.entity.Product;
import com.mart.entity.User;
import com.mart.repository.OrderDetailRepository;
import com.mart.repository.OrderRepository;
import com.mart.repository.ProductRepository;
import com.mart.repository.UserRepository;
import com.mart.response.ResponsePayment;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Override
	@Transactional
	public OrderDTO createOrder(long userId, String address, List<OrderDetail> orderDetails, String payment,
			String phone) {

		try {
			// Check if User exists
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			// Check product list
			if (orderDetails == null || orderDetails.isEmpty()) {
				throw new RuntimeException("Product list cannot be empty!");
			}

			// Create new Order
			Order order = new Order();
			order.setUser(user);
			order.setAddress(address);
			order.setPayment(payment);
			order.setPhone(phone);

			// Save Order to get ID before creating OrderDetail
			order = orderRepository.save(order);

			double totalPrice = 0;

			List<OrderDetail> savedOrderDetails = new ArrayList<>(); // Create a new list to store the OrderDetails

			for (OrderDetail data : orderDetails) {
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setQuantity(data.getQuantity());
				orderDetail.setProduct(data.getProduct());
				orderDetail.setOrder(order);

				// Assign ID after order.getId()
				KeyOrderDetail keyOrderDetail = new KeyOrderDetail(order.getId(), data.getProduct().getId());
				orderDetail.setKeyOrderDetail(keyOrderDetail);

				double priceProduct = data.getProduct().getPrice();
				totalPrice += data.getQuantity() * priceProduct;

				savedOrderDetails.add(orderDetail); // Save and add to list

				orderDetailRepository.save(orderDetail);
			}

			order.setPriceTotal(totalPrice); // Update total order value
			order.setOrderDetails(savedOrderDetails);
			orderRepository.save(order);

			return new OrderDTO(order);
		} catch (Exception e) {
			throw new RuntimeException("Order creation error: " + e.getMessage(), e);
		}
	}

	@Override
	@Transactional
	public OrderDTO updateOrder(long orderId, long userId, String updatedDate, String address,
			List<OrderDetail> orderDetails, String payment, String phone) {

		try {

			// Check if Order exists
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			// Check if User exists
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updated_date = simpleDateFormat.parse(updatedDate);
			order.setUpdatedDate(updated_date);

			order.setPhone(phone);
			order.setAddress(address);
			order.setPayment(payment);
			order.setUser(user);

			if (orderDetails == null || orderDetails.isEmpty()) {
				throw new RuntimeException("Product list cannot be empty!");
			}

			orderDetailRepository.deleteByOrderId(order.getId());

			double priceTotal = 0;

			List<OrderDetail> savedOrderDetails = new ArrayList<>(); // Create a new list to store the OrderDetails

			for (OrderDetail data : orderDetails) {

				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setQuantity(data.getQuantity());
				orderDetail.setProduct(data.getProduct());
				orderDetail.setOrder(order);

				KeyOrderDetail keyOrderDetail = new KeyOrderDetail();
				keyOrderDetail.setOrderId(order.getId());
				keyOrderDetail.setProductId(data.getProduct().getId());
				orderDetail.setKeyOrderDetail(keyOrderDetail);

				double priceProduct = data.getProduct().getPrice();
				priceTotal += data.getQuantity() * priceProduct;

				savedOrderDetails.add(orderDetail);

				orderDetailRepository.save(orderDetail);

			}

			order.setPriceTotal(priceTotal);
			order.setOrderDetails(savedOrderDetails);
			orderRepository.save(order);
			return new OrderDTO(order);
		} catch (Exception e) {
			throw new RuntimeException("Error updating order: " + e.getMessage(), e);
		}

	}

	@Transactional
	@Override
	public boolean deleteOrder(long orderId) {

		try {

			// Kiểm tra Order có tồn tại không
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			orderDetailRepository.deleteByOrderId(orderId);
			orderRepository.deleteById(orderId);

			return true;

		} catch (Exception e) {
			throw new RuntimeException("Error deleting order: " + e.getMessage(), e);
		}

	}

	@Override
	public List<OrderDTO> getOrderByUserId(long userId) {

		List<Order> orders = orderRepository.getOrderByUserId(userId);

		if (orders.isEmpty()) {
			throw new RuntimeException("No orders!");
		}

		return orders.stream().map(OrderDTO::new).collect(Collectors.toList());
	}

	@Override
	public OrderDTO changeOrderStatus(long userId, long orderId, String status) {

		try {

			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			order.setStatus(status);
			orderRepository.save(order);
			return OrderDTO.toBasicOrderDTO(order);

		} catch (Exception e) {
			throw new RuntimeException("Lỗi thay đổi trạng thái đơn hàng: " + e.getMessage(), e);
		}

	}

	@Override
	public String orderPayment(long orderId) {
		try {
			Optional<Order> orderOptional = orderRepository.findById(orderId);

			if (orderOptional.isEmpty()) {
				throw new RuntimeException("Order not found: " + orderId);
			}

			Order order = orderOptional.get();

			if (!"Thanh toán online".equals(order.getPayment())) {
				throw new RuntimeException("Order is not for online payment: " + orderId);
			}

			long amount = ((long) order.getPriceTotal()) * 100;
			String url = "http://localhost:8080/payment/create?amount=" + amount + "&vnp_TxnRef=" + orderId;

			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(url, String.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
		}
	}

	public void updateOrderPaymentStatus(String orderId, String status) {
		Order order = orderRepository.findById(Integer.parseInt(orderId))
				.orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

		order.setStatus(status);
		orderRepository.save(order);
	}

}
