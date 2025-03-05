package com.mart.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mart.entity.Order;

public class OrderDTO {

	private int id;
	private double priceTotal;
	private Date createdDate;
	private Date updatedDate;
	private String address;
	private String status;
	private String payment;
	private String phone;
	private List<OrderDetailDTO> orderDetailDTOs;
	
	public OrderDTO(Order order) {
        this.id = order.getId();
        this.priceTotal = order.getPriceTotal();
        this.createdDate = order.getCreatedDate();
        this.updatedDate = order.getUpdatedDate();
        this.address = order.getAddress();
        this.status = order.getStatus();
        this.payment = order.getPayment();
        this.phone = order.getPhone();
        this.orderDetailDTOs = order.getOrderDetails()
                                    .stream()
                                    .map(OrderDetailDTO::new)
                                    .collect(Collectors.toList());
    }
	
	public OrderDTO() {
		super();
	}

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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public List<OrderDetailDTO> getOrderDetailDTOs() {
		return orderDetailDTOs;
	}
	public void setOrderDetailDTOs(List<OrderDetailDTO> orderDetailDTOs) {
		this.orderDetailDTOs = orderDetailDTOs;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
