package com.colpix.challenge.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class PasswordUpdateRequest {
	private static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty";
	
	@JsonProperty(required = true)
	@NotBlank(message = PASSWORD_CANNOT_BE_EMPTY)
	private String oldPassword;
	
	@JsonProperty(required = true)
	@NotBlank(message = PASSWORD_CANNOT_BE_EMPTY)
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
