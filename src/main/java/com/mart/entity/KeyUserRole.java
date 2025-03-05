package com.mart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class KeyUserRole {

	@Column(name = "user_id")
	private int userId;

	@Column(name = "role_id")
	private int roleId;

	public KeyUserRole() {
		super();
	}

	public KeyUserRole(int userId, int roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
