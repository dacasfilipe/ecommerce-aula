package com.senai.ecommerce.service;

import com.senai.ecommerce.dtos.UserDTO;
import com.senai.ecommerce.entities.User;
import com.senai.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return userToUserDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setBirthDate(LocalDate.parse(userDTO.getBirthDate()));
        user.setRoles(userDTO.getRoles());
        user = userRepository.save(user);
        return userToUserDTO(user);
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPhone(userDTO.getPhone());
        user.setBirthDate(LocalDate.parse(userDTO.getBirthDate()));
        user.setRoles(userDTO.getRoles());
        user = userRepository.save(user);
        return userToUserDTO(user);
    }

    public List<UserDTO> listUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::userToUserDTO).collect(Collectors.toList());
    }

    public List<UserDTO> searchUsers(String searchTerm) {
        List<User> users = userRepository.searchByName(searchTerm);
        return users.stream().map(this::userToUserDTO).collect(Collectors.toList());
    }

    private UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getBirthDate(), user.getRoles());
    }
}
