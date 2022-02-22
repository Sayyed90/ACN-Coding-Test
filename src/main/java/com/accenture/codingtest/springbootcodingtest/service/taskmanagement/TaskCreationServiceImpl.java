package com.accenture.codingtest.springbootcodingtest.service.taskmanagement;

import com.accenture.codingtest.springbootcodingtest.constant.CommonConstant;
import com.accenture.codingtest.springbootcodingtest.entity.Task;
import com.accenture.codingtest.springbootcodingtest.errormessages.ErrorMessages;
import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;
import com.accenture.codingtest.springbootcodingtest.repository.TaskRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class TaskCreationServiceImpl implements TaskCreationService{
    @Autowired
    TaskRespository taskRepository;

    @Async
    @Override
    public CompletableFuture<List<TaskDTO>> getaLL() {
       List<Task> listOfTask = taskRepository.findAll();
        List<TaskDTO> listOfTasks =new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        listOfTask.stream().forEach(each->{
            TaskDTO taskDTOs = modelMapper.map(each, TaskDTO.class);
            listOfTasks.add(taskDTOs);
        });
        return CompletableFuture.completedFuture(listOfTasks);
    }
    @Async
    @Override
    public CompletableFuture<TaskDTO> getByID(int id) {
        long ids=id;
        Optional<Task> getTask= taskRepository.findById(ids);
        Task tasks=null;
        TaskDTO taskDTO =null;
        if(getTask.isPresent()){
             tasks=getTask.get();
             ModelMapper modelMapper = new ModelMapper();
             modelMapper.getConfiguration().setAmbiguityIgnored(true);
             taskDTO = modelMapper.map(tasks, TaskDTO.class);
        }
        return  CompletableFuture.completedFuture(taskDTO);
    }
    @Async
    @Override
    public CompletableFuture<TaskDTO> getByLoggedInUserID(int id) {
        long ids=id;
        Optional<Task> getTask= taskRepository.findByUserLoggedInID(ids);
        Task tasks=null;
        TaskDTO taskDTO =null;
        if(getTask.isPresent()){
            tasks=getTask.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            taskDTO = modelMapper.map(tasks, TaskDTO.class);
        }
        return  CompletableFuture.completedFuture(taskDTO);
    }
    @Async
    @Override
    public CompletableFuture<Boolean> post(TaskDTO taskDTO) throws Exception {
        taskRepository.saveAndFlush(Associate(taskDTO));
        return CompletableFuture.completedFuture(true);
    }
    @Async
    @Override
    public CompletableFuture<String> updateByID(int id, TaskDTO taskDTO) {
        long ids=id;
        Optional<Task> savedTask=taskRepository.findById(ids);
        Task tasks=null;
        long projectId;
        if(savedTask.isPresent() && null!=taskDTO){
            tasks=savedTask.get();
            if(null!=taskDTO.getStatus()){
                tasks.setStatus(taskDTO.getStatus());
            }
            if(null!=taskDTO.getTitle()){
                tasks.setTitle(taskDTO.getTitle());
            }
            if(null!=taskDTO.getDescription()){
                tasks.setDescription(taskDTO.getDescription());
            }
            if(taskDTO.getUserId()!=0){
                long checkIfUserExist=taskDTO.getUserId();
                Optional<Task> getTask= taskRepository.findByUserLoggedInID(checkIfUserExist);
                if(getTask.isPresent()){
                    if(!getTask.get().getStatus().equalsIgnoreCase(CommonConstant.COMPLETED)){
                        return CompletableFuture.completedFuture(ErrorMessages.USER_ALREADY_ASSIGNED_TO_TASK_ERROR);
                    }else{
                        Task getTaskAssignedtoUser=getTask.get();
                        getTaskAssignedtoUser.setUser(0);
                        taskRepository.saveAndFlush(getTaskAssignedtoUser);
                        tasks.setUser(taskDTO.getUserId());
                    }
                }else{
                    tasks.setUser(taskDTO.getUserId());
                }
            }
            if(taskDTO.getProjectId()!=0){
                tasks.setProject(taskDTO.getProjectId());
            }
        }
        taskRepository.saveAndFlush(tasks);
        return CompletableFuture.completedFuture(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER);
    }
    @Async
    @Override
    public CompletableFuture<Boolean> deleteByTitle(int id) {
        long ids=id;
        taskRepository.deleteById(ids);
        taskRepository.flush();
        Boolean isDeleted=false;
        Task taskByTitle= taskRepository.getById(ids);
        if(null==taskByTitle){
            isDeleted=true;
        }
        return CompletableFuture.completedFuture(isDeleted);
    }

    @Override
    public CompletableFuture<Boolean> updateTaskByMember(TaskDTO taskDTO) {
        long ids=taskDTO.getUserId();
        Optional<Task> taskByUserID= taskRepository.findByUserLoggedInID(ids);
        Task tasks=null;
        if(taskByUserID.isPresent()){
           tasks=taskByUserID.get();
        }
        if(null!=tasks && null!= taskDTO){
                 if(null!=taskDTO.getStatus()){
                     tasks.setStatus(taskDTO.getStatus() );
                 }
        }
        taskRepository.saveAndFlush(tasks);
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> removeUserFromtask(long ids) {
        Optional<Task> getTask= taskRepository.findByUserLoggedInID(ids);
        if(getTask.isPresent()){
            Task getNewTask=getTask.get();
            getNewTask.setUser(0);
            taskRepository.saveAndFlush(getNewTask);
            return CompletableFuture.completedFuture(false);
        }
        return CompletableFuture.completedFuture(false);
    }

    private Task Associate(TaskDTO taskDTO) throws Exception {
        Task task=new Task();
        if(null==taskDTO.getStatus()){
            task.setStatus(CommonConstant.NOT_STARTED);
        }else{
            task.setStatus(taskDTO.getStatus());
        }
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setProject(taskDTO.getProjectId());
        return task;
    }
}
