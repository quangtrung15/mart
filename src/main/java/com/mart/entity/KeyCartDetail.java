package com.mart.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class KeyCartDetail implements Serializable {

	@Column(name = "cart_id")
	private int cartId;

	@Column(name = "product_id")
	private int productId;

	public KeyCartDetail(int cartId, int productId) {
		super();
		this.cartId = cartId;
		this.productId = productId;
	}

	public KeyCartDetail() {
		super();
	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

}
