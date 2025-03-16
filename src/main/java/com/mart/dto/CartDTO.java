package com.mart.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mart.entity.Cart;
import com.mart.entity.Category;

import lombok.Data;

@Data
public class CartDTO {

	private long id;
	private double priceTotal;
	private Date createdDate;
	private Date updatedDate;
	private List<CartDetailDTO> cartDetailDTOs;

	public CartDTO() {
		super();
	}

	public CartDTO(Cart cart) {
		this.id = cart.getId();
		this.priceTotal = cart.getPriceTotal();
		this.createdDate = cart.getCreatedDate();
		this.updatedDate = cart.getUpdatedDate();
		this.cartDetailDTOs = cart.getCartDetails().stream().map(CartDetailDTO::new).collect(Collectors.toList());
	}

	public static CartDTO toBasicCartDTO(Cart cart) {
		return new CartDTO(cart.getId(), cart.getPriceTotal(), cart.getCreatedDate(), cart.getUpdatedDate());
	}

	public CartDTO(long id, double priceTotal, Date createdDate, Date updatedDate) {
		super();
		this.id = id;
		this.priceTotal = priceTotal;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

}
