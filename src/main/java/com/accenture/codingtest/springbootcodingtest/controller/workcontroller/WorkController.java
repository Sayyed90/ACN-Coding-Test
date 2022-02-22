package com.accenture.codingtest.springbootcodingtest.controller.workcontroller;

import com.accenture.codingtest.springbootcodingtest.errormessages.ErrorMessages;
import com.accenture.codingtest.springbootcodingtest.model.TaskAssignmentDTO;
import com.accenture.codingtest.springbootcodingtest.service.taskassignmentmanagement.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CompletableFuture;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1/assign")
@PreAuthorize("hasAuthority('PRODUCT_OWNER')")
public class WorkController {

    @Autowired
    TaskAssignmentService taskAssignmentService;

    @PatchMapping
    public ResponseEntity<String> assignTaskToBoth(@RequestBody TaskAssignmentDTO taskAssignmentDTO) throws Exception {

        CompletableFuture<String> getResponse= taskAssignmentService.assignTask(taskAssignmentDTO);
        if(getResponse.get().equalsIgnoreCase(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR)){
            return new ResponseEntity<String>(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER, new HttpHeaders(), HttpStatus.OK);
    }
}
