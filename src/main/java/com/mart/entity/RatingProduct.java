package com.mart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "rating_products")
public class RatingProduct {
	
	@EmbeddedId
	KeyRatingProduct keyRatingProduct;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "rate_point")
	private int ratePoint;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	public KeyRatingProduct getKeyRatingProduct() {
		return keyRatingProduct;
	}

	public void setKeyRatingProduct(KeyRatingProduct keyRatingProduct) {
		this.keyRatingProduct = keyRatingProduct;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRatePoint() {
		return ratePoint;
	}

	public void setRatePoint(int ratePoint) {
		this.ratePoint = ratePoint;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
	
}
