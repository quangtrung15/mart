package com.mart.dto;

import com.mart.entity.RatingProduct;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RatingProductDTO {

	private String comment;
	private int ratePoint;
	private String username;
	private String productName;
	
	public RatingProductDTO(RatingProduct ratingProduct) {
		this.comment = ratingProduct.getComment();
		this.ratePoint = ratingProduct.getRatePoint();
		this.username = ratingProduct.getUser().getFullName();
		this.productName = ratingProduct.getProduct().getName();
	}

}
