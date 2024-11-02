package com.example.task_for_VitaSoft.dto;

import com.example.task_for_VitaSoft.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    private Long userId;
    private String name;
    private String email;
    private String password;
    private List<Role> role;
}
