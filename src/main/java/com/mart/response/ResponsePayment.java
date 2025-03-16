package com.mart.response;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponsePayment implements Serializable {

	private String status;
	private String message;
	private String URL;

	public ResponsePayment(String status, String message, String uRL) {
		super();
		this.status = status;
		this.message = message;
		URL = uRL;
	}

	public ResponsePayment() {
		super();
	}

}
