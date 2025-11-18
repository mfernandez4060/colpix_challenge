package com.colpix.challenge.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

	private static final String PASSWORD_CANNOT_BE_EMPTY = "Password cannot be empty";
	private static final String USER_NAME_CANNOT_BE_EMPTY = "UserName cannot be empty";

	@JsonProperty(required = true)
	@NotBlank(message = USER_NAME_CANNOT_BE_EMPTY)
    private String username;
	
	@JsonProperty(required = true)
	@NotBlank(message = PASSWORD_CANNOT_BE_EMPTY)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}
}
