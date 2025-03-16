package com.mart.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "price_total")
	private double priceTotal;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "address")
	private String address;

	@Column(name = "status")
	private String status;

	@Column(name = "payment")
	private String payment;

	@Column(name = "phone")
	private String phone;

	@OneToMany(mappedBy = "order")
	private List<OrderDetail> orderDetails;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@PrePersist
	public void prePersist() {
		if (this.status == null || this.status.trim().isEmpty()) {
			this.status = "Đang chờ xử lý"; // Đặt giá trị mặc định nếu chưa có
		}
		if (this.createdDate == null) {
			this.createdDate = new Date(); // Chỉ gán nếu giá trị chưa được đặt
		}
	}

}
