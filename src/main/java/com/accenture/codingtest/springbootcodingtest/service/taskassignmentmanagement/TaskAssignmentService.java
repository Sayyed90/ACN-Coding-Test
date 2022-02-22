package com.accenture.codingtest.springbootcodingtest.service.taskassignmentmanagement;

import com.accenture.codingtest.springbootcodingtest.model.TaskAssignmentDTO;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface TaskAssignmentService {

    CompletableFuture<String> assignTask(TaskAssignmentDTO taskAssignmentDTO) throws InterruptedException, ExecutionException;
}
