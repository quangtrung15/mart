package com.mart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.entity.OrderDetail;
import com.mart.response.ResponseData;
import com.mart.service.OrderService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	@PostMapping("/create")
	public ResponseEntity<?> createOrder(@RequestParam int userId, @RequestParam String address,
			@RequestBody List<OrderDetail> orderDetails, @RequestParam String payment, @RequestParam String phone) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.createOrder(userId, address, orderDetails, payment, phone));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateOrder(@RequestParam int orderId, @RequestParam int userId,
			@RequestParam String updatedDate, @RequestParam String address, @RequestBody List<OrderDetail> orderDetails,
			@RequestParam String payment, @RequestParam String phone) {

		ResponseData responseData = new ResponseData();
		responseData
				.setData(orderService.updateOrder(orderId, userId, updatedDate, address, orderDetails, payment, phone));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteOrder(@RequestParam int orderId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.deleteOrder(orderId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@GetMapping("/get-order-by-user")
	public ResponseEntity<?> getOrderByUserId(@RequestParam int userId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.getOrderByUserId(userId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PostMapping("/payment")
	public ResponseEntity<?> orderPayment(@RequestParam int orderId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.orderPayment(orderId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

}
