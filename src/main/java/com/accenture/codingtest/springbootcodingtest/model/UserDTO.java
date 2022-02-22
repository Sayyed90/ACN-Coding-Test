package com.accenture.codingtest.springbootcodingtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;

    @JsonProperty("task")
    private TaskDTO task;
    @JsonProperty("roles")
    private List<RolesDTO> roles;

    @JsonIgnore
    private int id;

    public UserDTO(String myName, String s) {
        this.username=myName;
        this.password=s;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public List<RolesDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesDTO> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
