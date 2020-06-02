package com.example.animalsitter.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum StatusEnum {

	STATUS_ONE("VISIBLE"),
	STATUS_TWO("ACCEPTED"),
	STATUS_THREE("FINISHED");
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
