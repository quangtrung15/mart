package com.mart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mart.request.ResponseData;
import com.mart.service.CategoryService;
import com.mart.service.OrderService;
import com.mart.service.ProductService;

@RestController
@RequestMapping("/manage")
public class ManagerController {

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@PutMapping("/change-status-order")
	public ResponseEntity<?> changeOrderStatus(@RequestParam int userId, @RequestParam int orderId,
			@RequestParam String status) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.changeOrderStatus(userId, orderId, status));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PostMapping("/add-product")
	public ResponseEntity<?> addProduct(@RequestParam String name, @RequestParam MultipartFile file,
			@RequestParam String description, @RequestParam double price, @RequestParam int quantity,
			@RequestParam int promo, @RequestParam String status, @RequestParam String brand,
			@RequestParam int categoryId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(
				productService.addProduct(name, file, description, price, quantity, promo, status, brand, categoryId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PostMapping("/add-category")
	public ResponseEntity<?> addCategory(@RequestParam String name) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.addCategory(name));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

}
