package com.mart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.entity.CartDetail;
import com.mart.request.ResponseData;
import com.mart.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	CartService cartService;

	@PostMapping("/create")
	public ResponseEntity<?> createCart(@RequestParam int userId, @RequestParam String createdDate,
			@RequestBody List<CartDetail> cartDetails) {

		ResponseData responseData = new ResponseData();
		responseData.setData(cartService.createCart(userId, createdDate, cartDetails));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateCart(@RequestParam int userId, @RequestParam int cartId,
			@RequestParam String updatedDate, @RequestBody List<CartDetail> cartDetails) {

		ResponseData responseData = new ResponseData();
		responseData.setData(cartService.updateCart(userId, cartId, updatedDate, cartDetails));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCart(@RequestParam int cartId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(cartService.deleteCart(cartId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

	@GetMapping("/get")
	public ResponseEntity<?> getCartByUserId(int userId) {

		ResponseData responseData = new ResponseData();
		responseData.setData(cartService.getCartByUserId(userId));
		responseData.setSuccess(true);
		return new ResponseEntity(responseData, HttpStatus.OK);

	}

}
