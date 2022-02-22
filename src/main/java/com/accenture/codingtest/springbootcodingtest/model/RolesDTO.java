package com.accenture.codingtest.springbootcodingtest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RolesDTO {
    @JsonProperty("role")
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
