package com.mart.entity;

import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class KeyUserRole {

	@Column(name = "user_id")
	private int userId;

	@Column(name = "role_id")
	private int roleId;

}
