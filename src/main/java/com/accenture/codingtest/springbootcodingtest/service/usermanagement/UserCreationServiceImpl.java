package com.accenture.codingtest.springbootcodingtest.service.usermanagement;

import com.accenture.codingtest.springbootcodingtest.entity.Roles;
import com.accenture.codingtest.springbootcodingtest.entity.User;
import com.accenture.codingtest.springbootcodingtest.model.RolesDTO;
import com.accenture.codingtest.springbootcodingtest.model.UserDTO;
import com.accenture.codingtest.springbootcodingtest.repository.RoleRepository;
import com.accenture.codingtest.springbootcodingtest.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Transactional
@Service
public class UserCreationServiceImpl implements UserCreationService {

    Cipher cipher;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Async
    @Override
    public CompletableFuture<List<UserDTO>> getaLL() {
        List<UserDTO> listOfUser=new ArrayList<>();
        List<User> listOfUserFromDB = userRepository.findAll();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        listOfUserFromDB.stream().forEach(each->{
            UserDTO userDTOs = modelMapper.map(each, UserDTO.class);
            listOfUser.add(userDTOs);
        });
        return CompletableFuture.completedFuture(listOfUser);
    }
    @Async
    @Override
    public CompletableFuture<UserDTO> getByID(int id) {
        long ids=id;
        Optional<User> getUser= userRepository.findById(ids);
        User users=null;
        UserDTO userDTO =null;
        if(getUser.isPresent()) {
            users = getUser.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            userDTO = modelMapper.map(users, UserDTO.class);
        }
        return  CompletableFuture.completedFuture(userDTO);
    }
    @Async
    @Override
    public CompletableFuture<Boolean> post(UserDTO userDTO) throws Exception {
        String username=userDTO.getUsername();
        String password=userDTO.getPassword();
        if(null==username || username.isEmpty()){
            throw new Exception("Username is Empty");
        }if(null==password || password.isEmpty()){
            throw new Exception("Password is Empty");
        }if(null!=userDTO.getRoles()){
            if(userDTO.getRoles().size()<1){
                throw new Exception("No Roles is Assigned to user");
            }
        }
        userRepository.saveAndFlush(mapUser(userDTO));
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> updateByID(int id,UserDTO userDTO) throws Exception {
        long ids=id;
        Optional<User> getUser= userRepository.findById(ids);
        User users=null;

         if(getUser.isPresent()){
             users=getUser.get();
         }

         if(null!=users && null!=userDTO){
            if(null!=userDTO.getUsername() && !userDTO.getUsername().isEmpty()){
                users.setUsername(userDTO.getUsername());
            }
            if(null!=userDTO.getPassword() && !userDTO.getPassword().isEmpty()){
                users.setPassword(passwordEncoder().encode(userDTO.getPassword()));
            }
            if(null!=userDTO.getRoles() && !userDTO.getRoles().isEmpty()){
                Set<Roles> getRolesFromRepo=users.getRoles();
                List<RolesDTO> getRolesFromAdmin=userDTO.getRoles();
                getRolesFromAdmin.forEach(eachNewRole->{
                    roleRepository.flush();
                    Roles role =new Roles();
                    role.setRole(eachNewRole.getRole());
                    getRolesFromRepo.add(role);
                });
                users.setRoles(getRolesFromRepo);
            }
            userRepository.saveAndFlush(users);
        }
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> deleteByID(int id) {
        long ids=id;
        userRepository.deleteById(ids);
        userRepository.flush();
        Boolean isDeleted=false;
        Optional<User> userbyId= userRepository.findById(ids);
        if(userbyId.isPresent()){
            isDeleted=true;
        }
        return CompletableFuture.completedFuture(isDeleted);
    }

    @Override
    public CompletableFuture<Integer> findByUsername(String userName) {
        Optional<User> getUser= userRepository.findByUsername(userName);
        User getU=null;
        if(getUser.isPresent()){
            getU=getUser.get();
        }
        int id= (int) getU.getId();
        Integer idValue=id;
        return CompletableFuture.completedFuture(idValue);
    }

    private User mapUser(UserDTO userDto) throws Exception {
        User user=new User();
        if(null!=userDto.getUsername() && !userDto.getUsername().isEmpty()){
            user.setUsername(userDto.getUsername());
        }
        if(null!=userDto.getPassword() && !userDto.getPassword().isEmpty()){
            user.setPassword(passwordEncoder().encode(userDto.getPassword()));
        }
        if(null!=userDto.getRoles() && !userDto.getRoles().isEmpty()){
            user.setRoles(getAllRoles(userDto.getRoles()));
        }
        return user;
    }

    private Set<Roles> getAllRoles(List<RolesDTO> rolesDto){
        Set<Roles> rolesModel=new HashSet<>();
        Roles roles=new Roles();
        rolesDto.forEach(eachRole->{
            roles.setRole(eachRole.getRole());
            rolesModel.add(roles);
        });
        return rolesModel;
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
