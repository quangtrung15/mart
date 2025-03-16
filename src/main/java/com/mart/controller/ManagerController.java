package com.mart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mart.response.ResponseData;
import com.mart.service.CategoryService;
import com.mart.service.OrderService;
import com.mart.service.ProductService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	@Autowired
	OrderService orderService;

	@Autowired
	ProductService productService;

	@Autowired
	CategoryService categoryService;

	@PostMapping("/add-category")
	public ResponseEntity<?> addCategory(@RequestParam String name) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.addCategory(name));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PutMapping("/update-category")
	public ResponseEntity<?> updateCategory(@RequestParam int categoryId, @RequestParam String name) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.updateCategory(categoryId, name));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@DeleteMapping("/delete-category")
	public ResponseEntity<?> deleteCategory(@RequestParam int categoryId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.deleteCategory(categoryId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@GetMapping("/get-product-by-id")
	public ResponseEntity<?> findById(@RequestParam int productId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.findById(productId));
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

	@PutMapping("/update-product")
	public ResponseEntity<?> updateProduct(@RequestParam int productId, @RequestParam String name,
			@RequestParam MultipartFile file, @RequestParam String description, @RequestParam double price,
			@RequestParam int quantity, @RequestParam int promo, @RequestParam String status,
			@RequestParam String brand, @RequestParam int categoryId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.updateProduct(productId, name, file, description, price, quantity, promo,
				status, brand, categoryId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@DeleteMapping("/delete-product")
	public ResponseEntity<?> deleteById(@RequestParam int productId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.deleteById(productId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PutMapping("/change-status-order")
	public ResponseEntity<?> changeOrderStatus(@RequestParam int userId, @RequestParam int orderId,
			@RequestParam String status) {

		ResponseData responseData = new ResponseData();
		responseData.setData(orderService.changeOrderStatus(userId, orderId, status));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

}
