package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.UserDto;
import com.example.task_for_VitaSoft.dto.UserForAdminDto;
import com.example.task_for_VitaSoft.mapper.UserMapper;
import com.example.task_for_VitaSoft.service.CurrentUserService;
import com.example.task_for_VitaSoft.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;
    private final CurrentUserService currentUserService;

    /** ищем пользователя по имени */
    @GetMapping("/search")
    @Secured({"ADMIN"})
    public List<UserForAdminDto> getUsersByName(@RequestParam String name) {
        return UserMapper.toUserDtoList(userService.getUserByNameSearch(name));
    }

    /** назначаем роль OPERATOR */
    @PatchMapping("/users/{userId}")
    @Secured({"ADMIN"})
    public UserDto assignRole(@PathVariable Long userId) {
        final long adminId = currentUserService.getCurrentUser().getUserId();
        log.debug("");
        return UserMapper.toUserDto(userService.assignRole(userService.getUserById(adminId).getUserId(),
                userService.getUserById(userId).getUserId()));
    }

    /** получаем список пользователей */
    @GetMapping("/all")
    @Secured({"ADMIN"})
    public List<UserForAdminDto> getAllUsers() {
        log.debug("");
        return UserMapper.toUserDtoList(userService.getUsers());
    }

    /** получаем пользователя по id */
    @GetMapping("/{id}")
    @Secured({"ADMIN"})
    public UserDto getUserDtoById(@PathVariable long id) {
        log.debug("");
        return UserMapper.toUserDto(userService.getUserById(id));
    }
}
