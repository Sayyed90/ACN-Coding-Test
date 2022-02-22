package com.accenture.codingtest.springbootcodingtest.service.taskassignmentmanagement;

import com.accenture.codingtest.springbootcodingtest.errormessages.ErrorMessages;
import com.accenture.codingtest.springbootcodingtest.model.ProjectDTO;
import com.accenture.codingtest.springbootcodingtest.model.TaskAssignmentDTO;
import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;
import com.accenture.codingtest.springbootcodingtest.model.UserDTO;
import com.accenture.codingtest.springbootcodingtest.service.projectmanagement.ProjectCreationService;
import com.accenture.codingtest.springbootcodingtest.service.taskmanagement.TaskCreationService;
import com.accenture.codingtest.springbootcodingtest.service.usermanagement.UserCreationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class TaskAssignmentServiceImpl implements TaskAssignmentService{
    @Autowired
    UserCreationService userService;
    @Autowired
    TaskCreationService taskService;
    @Autowired
    ProjectCreationService projectService;

    @Async
    @Override
    public CompletableFuture<String> assignTask(TaskAssignmentDTO taskAssignmentDTO) throws ExecutionException, InterruptedException {

            ModelMapper modelMapper = new ModelMapper();
            int idOfUser= taskAssignmentDTO.getUserId();
            int idOfTask= taskAssignmentDTO.getTaskId();
            int idOfProject= taskAssignmentDTO.getProjectId();
            ProjectDTO projects=null;
            TaskDTO task=null;
            UserDTO user=null;
            if(idOfProject!=0){
                CompletableFuture<ProjectDTO> getProject= projectService.getById(idOfProject);
                try {
                    projects=getProject.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if(idOfTask!=0){
                CompletableFuture<TaskDTO> getTask= taskService.getByID(idOfTask);
                try {
                    task=getTask.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if(idOfUser!=0){
                CompletableFuture<UserDTO> getUser= userService.getByID(idOfUser);
                try {
                    user=getUser.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            if(null!=projects){
                task.setProjectId(projects.getId());
            }
            if(null!=user){
                task.setUserId(user.getId());
            }
            TaskDTO taskDTOs = modelMapper.map(task, TaskDTO.class);
        CompletableFuture<String> getResponse= taskService.updateByID(taskDTOs.getId(),taskDTOs);
        if(getResponse.get().equalsIgnoreCase(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR)){
            return CompletableFuture.completedFuture(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR);
        }
            return CompletableFuture.completedFuture(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER);
    }
}
