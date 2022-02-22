package com.accenture.codingtest.springbootcodingtest.userTest;

import com.accenture.codingtest.springbootcodingtest.errormessages.ErrorMessages;
import com.accenture.codingtest.springbootcodingtest.model.ProjectDTO;
import com.accenture.codingtest.springbootcodingtest.model.TaskAssignmentDTO;
import com.accenture.codingtest.springbootcodingtest.model.TaskDTO;
import com.accenture.codingtest.springbootcodingtest.model.UserDTO;
import com.accenture.codingtest.springbootcodingtest.principle.UserPrinciple;
import com.accenture.codingtest.springbootcodingtest.service.projectmanagement.ProjectCreationService;
import com.accenture.codingtest.springbootcodingtest.service.taskassignmentmanagement.TaskAssignmentService;
import com.accenture.codingtest.springbootcodingtest.service.taskmanagement.TaskCreationService;
import com.accenture.codingtest.springbootcodingtest.service.usermanagement.UserCreationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserCreationTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserPrinciple auth;

    @MockBean
    UserCreationService userCreationService;

    @MockBean
    ProjectCreationService projectCreationService;

    @MockBean
    TaskCreationService taskCreationService;

    @MockBean
    TaskAssignmentService tskAssignmentService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper mapper;

    String userJson="{\"username\":\"abc\",\"passwword\":\"123\"}";

    String createProject="{\"ProjectName\":\"Project twleve\",\n" +
            "    \"ProjectCode\":\"00012\"}";

    String createTask="{\"title\":\"Task 1\",\n" +
            "    \"description\":\"superman task\"}";

    String assignTask="{ \"task_id\":82 ,\n" +
            "    \"project_id\":74,\n" +
            "     \"user_id\":3}";

    String editStatus="{\"status\": \"IN_PROGRESS\"}";

    @Test
    @WithMockUser(username = "abc", password = "123",authorities="ADMIN")
    public void createUser() throws Exception {

        CompletableFuture<Boolean> ble=CompletableFuture.completedFuture(true);
        Mockito.when(userCreationService.post(Mockito.any(UserDTO.class))).thenReturn(ble);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/users").
                accept(MediaType.APPLICATION_JSON).content(userJson).contentType(MediaType.APPLICATION_JSON);
        MvcResult res=mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response=res.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    @WithMockUser(username = "abc", password = "123",authorities="PRODUCT_OWNER")
    public void createProjectAndAssignUser() throws Exception {
        CompletableFuture<Boolean> ble=CompletableFuture.completedFuture(true);
        CompletableFuture<String> response=CompletableFuture.completedFuture(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER);

        Mockito.when(userCreationService.post(Mockito.any(UserDTO.class))).thenReturn(ble);
        Mockito.when(projectCreationService.post(Mockito.any(ProjectDTO.class))).thenReturn(ble);
        Mockito.when(taskCreationService.post(Mockito.any(TaskDTO.class))).thenReturn(ble);
        Mockito.when(tskAssignmentService.assignTask(Mockito.any(TaskAssignmentDTO.class))).thenReturn(response);

        RequestBuilder requestBuilderProjectCreation = MockMvcRequestBuilders.post("/api/v1/projects").
                accept(MediaType.APPLICATION_JSON).content(createProject).contentType(MediaType.APPLICATION_JSON);

        RequestBuilder requestBuilderTaskCreation = MockMvcRequestBuilders.post("/api/v1/tasks").
               accept(MediaType.APPLICATION_JSON).content(createTask).contentType(MediaType.APPLICATION_JSON);

       RequestBuilder requestBuilderAssignment = MockMvcRequestBuilders.patch("/api/v1/assign").
               accept(MediaType.APPLICATION_JSON).content(assignTask).contentType(MediaType.APPLICATION_JSON);

        MvcResult resForTaskCreate=mockMvc.perform(requestBuilderTaskCreation).andReturn();
        MvcResult resForProjectCreate=mockMvc.perform(requestBuilderProjectCreation).andReturn();
        MvcResult resForAssignTaskToProject=mockMvc.perform(requestBuilderAssignment).andReturn();

        MockHttpServletResponse responseForTaskCreation=resForTaskCreate.getResponse();
        MockHttpServletResponse responseForProjectCreation=resForProjectCreate.getResponse();
        MockHttpServletResponse responseForProjectAssignment=resForAssignTaskToProject.getResponse();

        assertEquals(HttpStatus.OK.value(),responseForTaskCreation.getStatus());
        assertEquals(HttpStatus.OK.value(),responseForProjectCreation.getStatus());
        assertEquals(HttpStatus.OK.value(),responseForProjectAssignment.getStatus());
        assertEquals(ErrorMessages.SUCCESSFULLY_ASSIGNED_TASK_TO_USER,responseForProjectAssignment.getContentAsString());
    }

    @Test
    @WithMockUser(username = "abc", password = "123",authorities="MEMBER")
    public void memberEditStatus() throws Exception {

        CompletableFuture<Boolean> ble=CompletableFuture.completedFuture(true);
        Mockito.when(taskCreationService.updateTaskByMember(Mockito.any(TaskDTO.class))).thenReturn(ble);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/v1/member/modify").
                accept(MediaType.APPLICATION_JSON).content(editStatus).contentType(MediaType.APPLICATION_JSON);

        MvcResult res=mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response=res.getResponse();
        assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
}
