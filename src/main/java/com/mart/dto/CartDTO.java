package com.mart.dto;

import java.util.Date;
import java.util.List;

public class CartDTO {

	private int id;
	private double priceTotal;
	private Date createdDate;
	private Date updatedDate;
	private List<CartDetailDTO> cartDetailDTOs;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getPriceTotal() {
		return priceTotal;
	}
	public void setPriceTotal(double priceTotal) {
		this.priceTotal = priceTotal;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public List<CartDetailDTO> getCartDetailDTOs() {
		return cartDetailDTOs;
	}
	public void setCartDetailDTOs(List<CartDetailDTO> cartDetailDTOs) {
		this.cartDetailDTOs = cartDetailDTOs;
	}
	
}
