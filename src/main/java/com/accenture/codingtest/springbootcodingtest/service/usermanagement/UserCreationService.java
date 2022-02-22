package com.accenture.codingtest.springbootcodingtest.service.usermanagement;

import com.accenture.codingtest.springbootcodingtest.model.UserDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserCreationService {
    CompletableFuture<List<UserDTO>> getaLL();
    CompletableFuture<UserDTO> getByID(int id);
    CompletableFuture<Boolean> post(UserDTO userDTO) throws Exception;
    CompletableFuture<Boolean> updateByID(int id,UserDTO userDTO) throws Exception;
    CompletableFuture<Boolean> deleteByID(int id);
    CompletableFuture<Integer> findByUsername(String userName);
}
