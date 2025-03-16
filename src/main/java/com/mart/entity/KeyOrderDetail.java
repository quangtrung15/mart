package com.mart.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KeyOrderDetail implements Serializable {

	@Column(name = "order_id")
	private long orderId;

	@Column(name = "product_id")
	private long productId;

}
