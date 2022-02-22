package com.accenture.codingtest.springbootcodingtest.service.projectmanagement;

import com.accenture.codingtest.springbootcodingtest.model.ProjectDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ProjectCreationService {
    CompletableFuture<List<ProjectDTO>> getaLL(Integer pageNumber, Integer pageSizes, String sortBy);
    CompletableFuture<ProjectDTO> getById(int id);
    CompletableFuture<Boolean> post(ProjectDTO projectDTO) throws Exception;
    CompletableFuture<Boolean> updateByTitle(int id, ProjectDTO projectDTO);
    CompletableFuture<Boolean> deleteByTitle(int id);
    CompletableFuture<List<ProjectDTO>> getaLLs();
}
