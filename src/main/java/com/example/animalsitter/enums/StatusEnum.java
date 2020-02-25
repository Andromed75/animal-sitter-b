package com.example.animalsitter.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum StatusEnum {

	STATUS_ONE("ONE"),
	STATUS_TWO("TWO"),
	STATUS_THREE("THREE");
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
