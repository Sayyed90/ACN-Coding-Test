package com.accenture.codingtest.springbootcodingtest.service.taskmanagement;

import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TaskCreationService {
    CompletableFuture<List<TaskDTO>> getaLL();
    CompletableFuture<TaskDTO> getByID(int id);
    CompletableFuture<TaskDTO> getByLoggedInUserID(int id);
    CompletableFuture<Boolean> post(TaskDTO taskDTO) throws Exception;
    CompletableFuture<String> updateByID(int id, TaskDTO taskDTO);
    CompletableFuture<Boolean> deleteByTitle(int id);
    CompletableFuture<Boolean> updateTaskByMember(TaskDTO taskDTO);

    CompletableFuture<Boolean> removeUserFromtask(long ids);
}
