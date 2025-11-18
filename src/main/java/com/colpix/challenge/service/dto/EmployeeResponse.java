package com.colpix.challenge.service.dto;

import java.time.LocalDateTime;

public class EmployeeResponse {

    private Long id;
    private String name;
    private String email;
    private Long supervisorId;
    private LocalDateTime updatedAt;
    private Integer subordinatesCount;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String name, String email, Long supervisorId, LocalDateTime updatedAt, Integer subordinatesCount) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.supervisorId = supervisorId;
        this.updatedAt = updatedAt;
        this.subordinatesCount = subordinatesCount;
    }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSubordinatesCount() {
        return subordinatesCount;
    }

    public void setSubordinatesCount(Integer subordinatesCount) {
        this.subordinatesCount = subordinatesCount;
    }
}
