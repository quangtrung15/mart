package com.mart.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.response.ResponseData;
import com.mart.service.CategoryService;
import com.mart.service.FileService;
import com.mart.service.ProductService;

@RestController
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@Autowired
	FileService fileService;

	// Display a list of categories
	@GetMapping("/get-category")
	public ResponseEntity<?> getCategory() {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.getCategory());
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

	// Display product list by category
	@GetMapping("/products-by-category")
	public ResponseEntity<?> getProductsByCategoryId(@RequestParam int categoryId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(categoryService.getProductsByCategoryId(categoryId));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

	@GetMapping("/search")
	public ResponseEntity<?> findByProducts(@RequestParam(required = false) Long category_id,
			@RequestParam(required = false) Double minPrice, @RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String name, @RequestParam(required = false) String brand) {

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

	@GetMapping("/files/{filename:.+}")
	public ResponseEntity<?> getFile(@PathVariable String filename) throws UnsupportedEncodingException {

		Resource resource = fileService.loadFile(filename);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename*=UTF-8''" + URLEncoder.encode(resource.getFilename(), "UTF-8"))
				.body(resource);

	}

	@GetMapping("/find-product")
	public ResponseEntity<?> findById(@RequestParam int productId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.findById(productId));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

	@GetMapping("/get-product")
	public ResponseEntity<?> getProduct() {

		ResponseData responseData = new ResponseData();
		responseData.setData(productService.getProduct());
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

}
