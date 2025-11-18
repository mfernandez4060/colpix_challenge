package com.colpix.challenge.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmployeeRequest {

    private static final String EMAIL_CANNOT_BE_EMPTY = "Email cannot be empty";
	private static final String EMAIL_FORMAT_IS_INVALID = "Email format is invalid";
	private static final String NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
	private static final String USER_NAME_CANNOT_BE_EMPTY = "UserName cannot be empty";

	private Long id;

	@JsonProperty(required = true)
    @NotBlank(message = USER_NAME_CANNOT_BE_EMPTY)
    private String userName;
    
	@JsonProperty(required = true)
    @NotBlank(message = NAME_CANNOT_BE_EMPTY)
    private String name;
    
	@JsonProperty(required = true)
    @Email(message = EMAIL_FORMAT_IS_INVALID)
    @NotBlank(message = EMAIL_CANNOT_BE_EMPTY)
    private String email;
    
    private Long supervisorId;
    
	private String password;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getSupervisorId() {
        return supervisorId;
    }

    public void setSupervisorId(Long supervisorId) {
        this.supervisorId = supervisorId;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "EmployeeRequest [id=" + id + ", userName=" + userName + ", name=" + name + ", email=" + email
				+ ", supervisorId=" + supervisorId + ", password=" + password + "]";
	}
	
	
}
