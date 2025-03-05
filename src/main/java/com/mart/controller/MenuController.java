package com.mart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.request.ResponseData;
import com.mart.service.CategoryService;
import com.mart.service.ProductService;

@RestController
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@GetMapping("/products-by-category")
	public ResponseEntity<?> getProductsByCategoryId(@RequestParam int id) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.getProductsByCategoryId(id));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

	@GetMapping("/search")
	public ResponseEntity<?> findByProducts(@RequestParam(required = false) Integer category_id,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String name, @RequestParam(required = false) String brand) {

		// Chỉ khi tham số không phải null thì mới sử dụng chúng trong truy vấn
		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.findByProducts(category_id, minPrice, maxPrice, name, brand));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);
	}

	@GetMapping("/products-by-status")
	public ResponseEntity<?> getProductsByStatus(@RequestParam String status) {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.getProductsByStatus(status));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

}
