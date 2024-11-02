package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.exception.UserNotFoundException;
import com.example.task_for_VitaSoft.exception.UserValidationException;
import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.model.enums.Role;
import com.example.task_for_VitaSoft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //получить список всех пользователей
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    //получаем юзера по имени/части имени
    @Override
    public List<User> getUserByNameSearch(String name) {
        List<User> users = new ArrayList<>();
        if (name.isEmpty()) {
            return users;
        }
        users = userRepository.findUserBySearch(name);
        return users;
    }

    //назначаем роль OPERATOR
    @Override
    public User assignRole(Long adminId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Такого пользователя не существует"));
        if (userRepository.findById(adminId).get().getRoles().contains(Role.ADMIN)) {
            List<Role> roles = user.getRoles();
            roles.add(Role.OPERATOR);
            userRepository.save(user);
        } else {
            throw new UserValidationException("У вас нет прав на изменение роли пользователя");
        }
        return user;
    }

    //получаем пользователя по id
    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("user не найден"));
    }


    //создание пользователя - дропнуть потом
    public User create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new UserValidationException("Отсутствует имя пользователя");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new UserValidationException("Не указан email");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(Role.USER));
        user = userRepository.save(user);
        return user;
    }
}
