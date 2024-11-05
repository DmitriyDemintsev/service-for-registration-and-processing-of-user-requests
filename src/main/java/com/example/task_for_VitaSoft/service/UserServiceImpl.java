package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.exception.UserNotFoundException;
import com.example.task_for_VitaSoft.exception.UserValidationException;
import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.model.enums.Role;
import com.example.task_for_VitaSoft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getUsers() {
        log.debug("Getting a list of all users");
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserByNameSearch(String name) {
        List<User> users = new ArrayList<>();
        if (name.isEmpty()) {
            log.debug("An empty request");
            return users;
        }
        log.debug("Search for a user by name");
        users = userRepository.findByNameContainingIgnoreCase(name);
        log.debug("User/users with the name " + name + " found");
        return users;
    }

    @Override
    public User assignRole(Long adminId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user does not exist"));
        log.debug("User with id {} ", userId + " is not in the database");
        if (userRepository.findById(adminId).get().getRoles().contains(Role.ADMIN)) {
            List<Role> roles = user.getRoles();
            log.debug("Changing the role of a user with an id {} ", userId);
            roles.add(Role.OPERATOR);
            userRepository.save(user);
        } else {
            log.debug("The user with the id {} ", adminId + " does not have the rights to change the role");
            throw new UserValidationException("There are no rights to change the user's role");
        }
        log.debug("The role of the user with id {} ", userId + " has been changed from USER to OPERATOR");
        return user;
    }

    @Override
    public User getUserById(long userId) {
        log.debug("Getting a user by id");
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("The user with the id {} " + userId
                        + " is not in the database"));
    }
}
