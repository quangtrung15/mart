package com.mart.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class KeyRatingProduct implements Serializable {

	@Column(name = "user_id")
	private int userId;
	
	@Column(name = "product_id")
	private int productId;

	public KeyRatingProduct(int userId, int productId) {
		super();
		this.userId = userId;
		this.productId = productId;
	}

	public KeyRatingProduct() {
		super();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	
	
}
