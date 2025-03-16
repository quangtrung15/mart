package com.mart.dto;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.mart.entity.Order;

import lombok.Data;

@Data
public class OrderDTO {

	private long id;
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
		this.orderDetailDTOs = order.getOrderDetails().stream().map(OrderDetailDTO::new).collect(Collectors.toList());
	}

	public static OrderDTO toBasicOrderDTO(Order order) {
		return new OrderDTO(order.getId(), order.getPriceTotal(), order.getCreatedDate(), order.getUpdatedDate(), order.getAddress(), order.getStatus(), order.getPayment(), order.getPayment());
	}

	public OrderDTO() {
		super();
	}

	public OrderDTO(long id, double priceTotal, Date createdDate, Date updatedDate, String address, String status,
			String payment, String phone) {
		super();
		this.id = id;
		this.priceTotal = priceTotal;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.address = address;
		this.status = status;
		this.payment = payment;
		this.phone = phone;
	}

}
