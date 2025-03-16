package com.mart.request;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

	private int id;
	private double priceTotal;
	private Date createdDate;
	private Date updatedDate;
	private int userId;
	private Set<CartDetailRequest> cartDetailRequests;
	
}
