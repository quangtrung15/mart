package com.mart.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mart.dto.OrderDTO;
import com.mart.entity.KeyOrderDetail;
import com.mart.entity.Order;
import com.mart.entity.OrderDetail;
import com.mart.entity.User;
import com.mart.repository.OrderDetailRepository;
import com.mart.repository.OrderRepository;
import com.mart.repository.UserRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrderDetailRepository orderDetailRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createOrder(int userId, String address, String createdDate, List<OrderDetail> orderDetails,
			String payment, String phone) {

		try {
			// Kiểm tra User có tồn tại không
			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			// Kiểm tra xem danh sách sản phẩm có null không.
			if (orderDetails == null || orderDetails.isEmpty()) {
				throw new RuntimeException("Danh sách sản phẩm không được để trống!");
			}

			// Tạo Order mới và gán thông tin User
			Order order = new Order();
			order.setUser(user);

			// Chuyển đổi createdDate từ String sang Date
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date created_date = simpleDateFormat.parse(createdDate);
			order.setCreatedDate(created_date);

			order.setAddress(address);
			order.setPayment(payment);
			order.setPhone(phone);

			// Lưu Order trước để có ID
			order = orderRepository.save(order);

			// Tính tổng giá trị hóa đơn
			double totalPrice = 0;
			for (OrderDetail data : orderDetails) {

				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setQuantity(data.getQuantity());
				orderDetail.setProduct(data.getProduct());
				orderDetail.setOrder(order);

				// Tạo khóa chính tổng hợp KeyOrderDetail
				KeyOrderDetail keyOrderDetail = new KeyOrderDetail();
				keyOrderDetail.setOrderId(order.getId());
				keyOrderDetail.setProductId(data.getProduct().getId());
				orderDetail.setKeyOrderDetail(keyOrderDetail);

				double priceProduct = data.getProduct().getPrice();
				totalPrice += data.getQuantity() * priceProduct;

				orderDetailRepository.save(orderDetail);
			}

			// Cập nhật tổng giá trị hóa đơn
			order.setPriceTotal(totalPrice);
			orderRepository.save(order); // Lưu lại Order sau khi cập nhật giá

			return true;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi hệ thống: " + e.getMessage(), e);
		}
	}

	@Transactional
	@Override
	public boolean updateOrder(int orderId, int userId, String updatedDate, String address,
			List<OrderDetail> orderDetails, String payment, String phone) {

		try {

			// Kiểm tra Order có tồn tại không
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			// Kiểm tra User có tồn tại không
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
				throw new RuntimeException("Danh sách sản phẩm không được để trống!");
			}

			orderDetailRepository.deleteByOrderId(order.getId());

			double priceTotal = 0;
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

				orderDetailRepository.save(orderDetail);

			}

			order.setPriceTotal(priceTotal);
			orderRepository.save(order);

			return true;
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi cập nhật đơn hàng: " + e.getMessage(), e);
		}

	}

	@Transactional
	@Override
	public boolean deleteOrder(int orderId) {

		try {

			// Kiểm tra Order có tồn tại không
			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			orderDetailRepository.deleteByOrderId(orderId);
			orderRepository.deleteById(orderId);

			return true;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi xóa đơn hàng: " + e.getMessage(), e);
		}

	}

//	@Override
//	public List<OrderDTO> getOrderByUserId(int userId) {
//
//		List<Order> orders = orderRepository.getOrderByUserId(userId);
//
//		if (orders == null || orders.isEmpty()) {
//			throw new RuntimeException("Không có sản phẩm nào!");
//		}
//
//		List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();
//
//		for (Order data : orders) {
//
//			OrderDTO orderDTO = new OrderDTO();
//			orderDTO.setId(data.getId());
//			orderDTO.setPriceTotal(data.getPriceTotal());
//			orderDTO.setCreatedDate(data.getCreatedDate());
//			orderDTO.setUpdatedDate(data.getUpdatedDate());
//			orderDTO.setAddress(data.getAddress());
//			orderDTO.setStatus(data.getStatus());
//			orderDTO.setPayment(data.getPayment());
//
//			List<OrderDetailDTO> orderDetailDTOs = new ArrayList();
//
//			for (OrderDetail data1 : data.getOrderDetails()) {
//
//				OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
//				orderDetailDTO.setQuantity(data1.getQuantity());
//
//				ProductDTO productDTO = new ProductDTO();
//				productDTO.setId(data1.getProduct().getId());
//				productDTO.setName(data1.getProduct().getName());
//				productDTO.setImage(data1.getProduct().getImage());
//				productDTO.setDescription(data1.getProduct().getDescription());
//				productDTO.setPrice(data1.getProduct().getPrice());
//				productDTO.setQuantity(data1.getProduct().getQuantity());
//				productDTO.setPromo(data1.getProduct().getPromo());
//				productDTO.setStatus(data1.getProduct().getStatus());
//				productDTO.setBrand(data1.getProduct().getBrand());
//				orderDetailDTO.setProductDTO(productDTO);
//
//				orderDetailDTOs.add(orderDetailDTO);
//			}
//			orderDTO.setOrderDetailDTOs(orderDetailDTOs);
//			orderDTOs.add(orderDTO);
//
//		}
//
//		return orderDTOs;
//	}

	@Override
	public List<OrderDTO> getOrderByUserId(int userId) {

		List<Order> orders = orderRepository.getOrderByUserId(userId);

		if (orders.isEmpty()) {
			throw new RuntimeException("Không có đơn hàng nào!");
		}

		return orders.stream().map(OrderDTO::new).collect(Collectors.toList());
	}

	@Override
	public boolean changeOrderStatus(int userId, int orderId, String status) {

		try {

			User user = userRepository.findById(userId)
					.orElseThrow(() -> new RuntimeException("User with ID " + userId + " not found!"));

			Order order = orderRepository.findById(orderId)
					.orElseThrow(() -> new RuntimeException("Order with ID " + orderId + " not found!"));

			order.setStatus(status);
			orderRepository.save(order);
			return true;

		} catch (Exception e) {
			throw new RuntimeException("Lỗi thay đổi trạng thái đơn hàng: " + e.getMessage(), e);
		}

	}

}
