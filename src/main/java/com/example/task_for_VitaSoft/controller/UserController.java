package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.UserDto;
import com.example.task_for_VitaSoft.dto.UserForAdminDto;
import com.example.task_for_VitaSoft.mapper.UserMapper;
import com.example.task_for_VitaSoft.model.User;
//import com.example.task_for_VitaSoft.security.UserDetailsImpl;
import com.example.task_for_VitaSoft.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {
    private final UserService userService;

    //ищем пользователя по имени
    @GetMapping("/search")
    public List<UserForAdminDto> getUsersByName(@RequestParam String name) {
        return UserMapper.toUserDtoList(userService.getUserByNameSearch(name));
    }

    //назначаем роль OPERATOR
    @PatchMapping("/{adminId}/users/{userId}")
    public UserDto assignRole(@PathVariable Long adminId, @PathVariable Long userId) {
        return UserMapper.toUserDto(userService.assignRole(userService.getUserById(adminId).getUserId(),
                userService.getUserById(userId).getUserId()));
    }

    //получаем список пользователей
    @GetMapping("/all")
    @Secured({"ADMIN"})
    public List<UserForAdminDto> getAllUsers() {
        return UserMapper.toUserDtoList(userService.getUsers());
    }

    //получаем пользователя по id
    @GetMapping("/{id}")

    public UserDto getUserDtoById(@PathVariable long id) {
//        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication()
//                .getPrincipal();
//        User requestor = userDetails.getUser();
//        System.out.println(requestor);
        return UserMapper.toUserDto(userService.getUserById(id));
    }
}
