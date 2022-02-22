package com.accenture.codingtest.springbootcodingtest.controller.projectcontroller;

import com.accenture.codingtest.springbootcodingtest.model.ProjectDTO;
import com.accenture.codingtest.springbootcodingtest.service.projectmanagement.ProjectCreationService;
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
@RequestMapping("/api/v1/projects")
@PreAuthorize("hasAuthority('PRODUCT_OWNER')")
public class ProjectController {
    @Autowired
    private ProjectCreationService projectCreationService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(defaultValue = "3") Integer pageSizes,
                                                   @RequestParam(defaultValue = "name") String sortBy) throws ExecutionException, InterruptedException {
        CompletableFuture<List<ProjectDTO>> getListOfUsersAsynchronously=projectCreationService.getaLL(pageNumber,pageSizes,sortBy);
        List<ProjectDTO> allProjects=null;
        if(getListOfUsersAsynchronously.get().size()>0){
            allProjects=getListOfUsersAsynchronously.get();
            return new ResponseEntity<List<ProjectDTO>>(allProjects, new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<List<ProjectDTO>>(allProjects, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{project_id}")
    public ResponseEntity<ProjectDTO> findProjectById(@PathVariable("project_id") int id) throws ExecutionException, InterruptedException {
        CompletableFuture<ProjectDTO> getprojectByCode=projectCreationService.getById(id);
        ProjectDTO task=getprojectByCode.get();
        return new ResponseEntity<ProjectDTO>(task, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<String> saveNewProjectIntoDB(@RequestBody ProjectDTO projectDTO) throws Exception {
        CompletableFuture<Boolean> postProjectToDB=projectCreationService.post(projectDTO);
        boolean isProjectSaved=postProjectToDB.get();
        if(isProjectSaved){
            return new ResponseEntity<String>("Successfully Saved Project...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Saved Project...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{project_id}")
    public ResponseEntity<String> updateProjectFullyByPut(@PathVariable("project_id") int id, @RequestBody ProjectDTO projectDTO) throws Exception {
        CompletableFuture<Boolean> isProjectUpdatedInTheDB=projectCreationService.updateByTitle(id,projectDTO);
        boolean isProjectUpdated=isProjectUpdatedInTheDB.get();
        if(isProjectUpdated){
            return new ResponseEntity<String>("Successfully Updated Project...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Updating Project...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{project_id}")
    public ResponseEntity<String> updateProjectPartiallyByPatch(@PathVariable("project_id") int id, @RequestBody ProjectDTO projectDTO) throws Exception {
        CompletableFuture<Boolean> isProjectUpdatedInTheDB=projectCreationService.updateByTitle(id,projectDTO);
        boolean isProjectUpdated=isProjectUpdatedInTheDB.get();
        if(isProjectUpdated){
            return new ResponseEntity<String>("Successfully Updated Project...", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Something went wrong when Updating Project...", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{project_id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable("project_id")  int id) throws Exception {
        CompletableFuture<Boolean> isDeleted=projectCreationService.deleteByTitle(id);
        boolean isProjectDeleted=isDeleted.get();
        if(isProjectDeleted){
            return new ResponseEntity<String>("Project Not successfully deleted", new HttpHeaders(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Project successfully deleted..", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
