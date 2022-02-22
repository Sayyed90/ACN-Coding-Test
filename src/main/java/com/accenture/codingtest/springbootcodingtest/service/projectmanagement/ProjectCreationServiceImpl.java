package com.accenture.codingtest.springbootcodingtest.service.projectmanagement;

import com.accenture.codingtest.springbootcodingtest.entity.Project;
import com.accenture.codingtest.springbootcodingtest.model.ProjectDTO;
import com.accenture.codingtest.springbootcodingtest.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
@Service
@Transactional
public class ProjectCreationServiceImpl implements ProjectCreationService {
    @Autowired
    ProjectRepository projectRepository;
    @Async
    @Override
    public CompletableFuture<List<ProjectDTO>> getaLL(Integer pageNumber, Integer pageSizes, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSizes, Sort.by(sortBy).ascending());
        Page<Project> pagedResult = projectRepository.findAll(paging);
        List<ProjectDTO> listOfProjects =new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        if(pagedResult.hasContent()) {
            List<Project> listOfPaged=pagedResult.getContent();
            listOfPaged.stream().forEach(each->{
                ProjectDTO projectDTO = modelMapper.map(each, ProjectDTO.class);
                listOfProjects.add(projectDTO);
            });
            return CompletableFuture.completedFuture(listOfProjects);
        } else {
            return new CompletableFuture<List<ProjectDTO>>();
        }
    }

    @Async
    @Override
    public CompletableFuture<ProjectDTO> getById(int id) {
        long ids=id;
        Optional<Project> projectByCode= Optional.of(projectRepository.getById(ids));
        Project getProject=null;
        ProjectDTO projectDTO =null;
       if(projectByCode.isPresent()){
          getProject=projectByCode.get();
          ModelMapper modelMapper = new ModelMapper();
          modelMapper.getConfiguration().setAmbiguityIgnored(true);
          projectDTO = modelMapper.map(getProject, ProjectDTO.class);
       }
        return CompletableFuture.completedFuture(projectDTO);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> post(ProjectDTO projectDTO) throws Exception {
        projectRepository.saveAndFlush(Associate(projectDTO));
        return CompletableFuture.completedFuture(true);
    }

    @Async
    @Override
    public CompletableFuture<Boolean> updateByTitle(int id, ProjectDTO projectDTO) {
        long ids=id;
        Optional<Project> projectByCode= Optional.of(projectRepository.getById(ids));
        Project projects=null;
        if(projectByCode.isPresent()){
            projects=projectByCode.get();
        }
        if(null!=projects & null!=projectDTO){
            if(null!=projectDTO.getName()){
                projects.setName(projectDTO.getName());
            }
            if(null!=projectDTO.getCode()){
                projects.setCode(projectDTO.getCode());
            }
        }
        projectRepository.saveAndFlush(projects);
        return CompletableFuture.completedFuture(true);
    }
    @Async
    @Override
    public CompletableFuture<Boolean> deleteByTitle(int id) {
        long ids=id;
        projectRepository.deleteById(ids);
        projectRepository.flush();
        Boolean isDeleted=false;
        Optional<Project> projectByTitle= projectRepository.findById(ids);
        if(projectByTitle.isPresent()){
            isDeleted=true;
        }
        return CompletableFuture.completedFuture(isDeleted);
    }

    @Override
    public CompletableFuture<List<ProjectDTO>> getaLLs() {

        List<Project> projectByCode= projectRepository.findAll();

        List<ProjectDTO> listOfProjects =new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        projectByCode.stream().forEach(each->{
            ProjectDTO taskDTOs = modelMapper.map(each, ProjectDTO.class);
            listOfProjects.add(taskDTOs);
        });
        return CompletableFuture.completedFuture(listOfProjects);
    }

    private Project Associate(ProjectDTO projectDTO) throws Exception {
        Project project= new Project();
        project.setName(projectDTO.getName());
        project.setCode(projectDTO.getCode());
        return project;
    }

}
