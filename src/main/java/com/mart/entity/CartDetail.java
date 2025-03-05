package com.mart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "cart_details")
public class CartDetail {

	@EmbeddedId
	KeyCartDetail keyCartDetail;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "cart_id", insertable = false, updatable = false)
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;

	public CartDetail(KeyCartDetail keyCartDetail, Cart cart, Product product) {
		super();
		this.keyCartDetail = keyCartDetail;
		this.cart = cart;
		this.product = product;
	}

	public CartDetail() {
		super();
	}

	public KeyCartDetail getKeyCartDetail() {
		return keyCartDetail;
	}

	public void setKeyCartDetail(KeyCartDetail keyCartDetail) {
		this.keyCartDetail = keyCartDetail;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public CartDetail(KeyCartDetail keyCartDetail, int quantity, Cart cart, Product product) {
		super();
		this.keyCartDetail = keyCartDetail;
		this.quantity = quantity;
		this.cart = cart;
		this.product = product;
	}

}
