package com.accenture.codingtest.springbootcodingtest.controller.membercontroller;

import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;
import com.accenture.codingtest.springbootcodingtest.service.taskmanagement.TaskCreationService;
import com.accenture.codingtest.springbootcodingtest.service.usermanagement.UserCreationService;
import com.accenture.codingtest.springbootcodingtest.sessionfetch.FetchUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1/member")
@PreAuthorize("hasAuthority('MEMBER')")
public class MemberController {
    @Autowired
    UserCreationService userCreationService;
    @Autowired
    TaskCreationService taskCreationService;

    @GetMapping("/getTask")
    public ResponseEntity<TaskDTO> getTaskAssigned() throws ExecutionException, InterruptedException {
        FetchUserDetails tab = new FetchUserDetails();
        String userName=tab.getNames();
        CompletableFuture<Integer> id=userCreationService.findByUsername(userName);
        int idOfUser=id.get();
        CompletableFuture<TaskDTO> isTaskEditedByUserUpdated=taskCreationService.getByLoggedInUserID(idOfUser);
        TaskDTO taskDTO=isTaskEditedByUserUpdated.get();
        if(null!=taskDTO){
            return new ResponseEntity<TaskDTO>(taskDTO, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<TaskDTO>(taskDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/modify")
    public ResponseEntity<String> modifyTaskStatus(@RequestBody TaskDTO taskDTO) throws ExecutionException, InterruptedException {
        FetchUserDetails tab = new FetchUserDetails();
        String userName=tab.getNames();
        CompletableFuture<Integer> id=userCreationService.findByUsername(userName);
        taskDTO.setUserId(id.get());
        CompletableFuture<Boolean> isTaskEditedByUserUpdated=taskCreationService.updateTaskByMember(taskDTO);
        boolean isTaskUpdated=isTaskEditedByUserUpdated.get();
        if(isTaskUpdated){
            return new ResponseEntity<String>("Successfully Modified Task...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Modifying Task...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
