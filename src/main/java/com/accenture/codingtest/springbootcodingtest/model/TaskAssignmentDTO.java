package com.accenture.codingtest.springbootcodingtest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TaskAssignmentDTO {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("task_id")
    private int taskId;
    @JsonProperty("project_id")
    private int projectId;

    public TaskAssignmentDTO(int projectId, int taskId, int userId) {
        this.projectId=projectId;
        this.taskId=taskId;
        this.userId=userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
