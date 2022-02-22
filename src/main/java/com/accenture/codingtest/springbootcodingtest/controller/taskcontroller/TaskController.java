package com.accenture.codingtest.springbootcodingtest.controller.taskcontroller;

import com.accenture.codingtest.springbootcodingtest.errormessages.ErrorMessages;
import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;
import com.accenture.codingtest.springbootcodingtest.service.taskmanagement.TaskCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/v1/tasks")
@PreAuthorize("hasAuthority('PRODUCT_OWNER')")
public class TaskController {
    @Autowired
    private TaskCreationService taskCreationService;

    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> getAllTask() throws ExecutionException, InterruptedException {
        CompletableFuture<List<TaskDTO>> getListOfUsersAsynchronously=taskCreationService.getaLL();
        List<TaskDTO> allUser=getListOfUsersAsynchronously.get();
        return new ResponseEntity<List<TaskDTO>>(allUser, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{Task_id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("Task_id") int id) throws ExecutionException, InterruptedException {
        CompletableFuture<TaskDTO> getUserByIdAsynchronously=taskCreationService.getByID(id);
        TaskDTO task=getUserByIdAsynchronously.get();
        return new ResponseEntity<TaskDTO>(task, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> saveNewTaskIntoDB(@RequestBody TaskDTO taskDTO) throws Exception {
        CompletableFuture<Boolean> postTaskToDB=taskCreationService.post(taskDTO);
        boolean isTaskSuccess=postTaskToDB.get();
        if(isTaskSuccess){
            return new ResponseEntity<String>("Successfully Saved Task...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Saved Task...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{Task_id}")
    public ResponseEntity<String> updateTaskFullyByPut(@PathVariable("Task_id") int id, @RequestBody TaskDTO userDTO) throws Exception {
        CompletableFuture<String> updateTaskInTheDB=taskCreationService.updateByID(id,userDTO);
        String response=updateTaskInTheDB.get();
       if(response.equalsIgnoreCase(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER)){
            return new ResponseEntity<String>("Successfully Updated Task...", new HttpHeaders(), HttpStatus.OK);
        }else if(response.equalsIgnoreCase(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR)){
           return new ResponseEntity<String>(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR, new HttpHeaders(), HttpStatus.BAD_REQUEST);
       }
        return new ResponseEntity<String>("Something went wrong when Updating Task...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/remove/{user_id}")
    public ResponseEntity<String> removeUserFromtask(@PathVariable("user_id") int id) throws Exception {
        long ids=id;
        CompletableFuture<Boolean> updateTaskInTheDB=taskCreationService.removeUserFromtask(ids);
        if(!updateTaskInTheDB.get()){
            return new ResponseEntity<String>("Successfully Removed Task...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when removing user from Task...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{Task_id}")
    public ResponseEntity<String> updateTaskPartiallyByPatch(@PathVariable("Task_id") int id, @RequestBody TaskDTO userDTO) throws Exception {
        CompletableFuture<String> updateTaskInTheDB=taskCreationService.updateByID(id,userDTO);
        String response=updateTaskInTheDB.get();
        if(response.equalsIgnoreCase(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER)){
            return new ResponseEntity<String>("Successfully Updated Task...", new HttpHeaders(), HttpStatus.OK);
        }else if(response.equalsIgnoreCase(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR)){
            return new ResponseEntity<String>(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("Something went wrong when Updating Task...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{Task_id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable("Task_id") int id) throws Exception {
        CompletableFuture<Boolean> isDeleted=taskCreationService.deleteByTitle(id);
        boolean isTaskDeleted=isDeleted.get();
        if(isTaskDeleted){
            return new ResponseEntity<String>("Task Failed to Delete..", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Task successfully deleted", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
