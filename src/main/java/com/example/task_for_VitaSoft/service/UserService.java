package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    List<User> getUserByNameSearch(String name);

    User assignRole(Long adminId, Long userId);

    User getUserById(long id);

    User create(User user); //для самоконтроля
}
