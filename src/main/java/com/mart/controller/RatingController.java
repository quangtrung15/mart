package com.mart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mart.request.ResponseData;
import com.mart.service.RatingProductService;

@RestController
@RequestMapping("/rating")
public class RatingController {

	@Autowired
	RatingProductService ratingProductService;

	@PostMapping("/rating-product")
	public ResponseEntity<?> ratingProduct(@RequestParam int userId, @RequestParam int productId,
			@RequestParam int ratePoint, @RequestParam String comment) {

		ResponseData responseData = new ResponseData();
		responseData.setData(ratingProductService.ratingProduct(userId, productId, ratePoint, comment));
		responseData.setSuccess(true);
		return new ResponseEntity<>(responseData, HttpStatus.OK);

	}

}
