package com.accenture.codingtest.springbootcodingtest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProjectDTO {
    @JsonProperty("ProjectName")
    private String name;
    @JsonProperty("ProjectCode")
    private String code;
    @JsonIgnore
    private int id;

    public ProjectDTO(String project1, String code1) {
        this.name=project1;
        this.code=code1;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
