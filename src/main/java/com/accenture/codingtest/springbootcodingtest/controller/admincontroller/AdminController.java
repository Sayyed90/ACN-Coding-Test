package com.accenture.codingtest.springbootcodingtest.controller.admincontroller;

import com.accenture.codingtest.springbootcodingtest.model.UserDTO;
import com.accenture.codingtest.springbootcodingtest.service.usermanagement.UserCreationService;
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
@RequestMapping("/api/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserCreationService adminServiceForUserCreation;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser() throws ExecutionException, InterruptedException {
        CompletableFuture<List<UserDTO>> getListOfUsersAsynchronously=adminServiceForUserCreation.getaLL();
        List<UserDTO> allUser=getListOfUsersAsynchronously.get();
        return new ResponseEntity<List<UserDTO>>(allUser, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable("user_id") int id) throws ExecutionException, InterruptedException {
        CompletableFuture<UserDTO> getUserByIdAsynchronously=adminServiceForUserCreation.getByID(id);
        UserDTO user=getUserByIdAsynchronously.get();
        return new ResponseEntity<UserDTO>(user, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveNewUserIntoDB(@RequestBody UserDTO userDTO) throws Exception {
        CompletableFuture<Boolean> postUserToDB=adminServiceForUserCreation.post(userDTO);
        boolean isSaved=postUserToDB.get();
        if(isSaved){
            return new ResponseEntity<String>("Successfully Saved User...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Saved User...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<String> updateUserFullyByPut(@PathVariable("user_id") int id, @RequestBody UserDTO userDTO) throws Exception {
        CompletableFuture<Boolean> updateUserInTheDB=adminServiceForUserCreation.updateByID(id,userDTO);
        boolean isUpdated=updateUserInTheDB.get();
        if(isUpdated){
            return new ResponseEntity<String>("Successfully Updated User...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Updating User...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<String> updateUserPartiallyByPatch(@PathVariable("user_id") int id, @RequestBody UserDTO userDTO) throws Exception {
        CompletableFuture<Boolean> updateUserInTheDB=adminServiceForUserCreation.updateByID(id,userDTO);
        boolean isUpdated=updateUserInTheDB.get();
        if(isUpdated){
            return new ResponseEntity<String>("Successfully Updated User...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Updating User...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<String> deleteUserByID(@PathVariable("user_id") int id) throws Exception {
        CompletableFuture<Boolean> isDeleted=adminServiceForUserCreation.deleteByID(id);
        boolean isUserDeleted=isDeleted.get();
        if(!isUserDeleted){
            return new ResponseEntity<String>("User not deleted Successfully", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("User successfully deleted", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
