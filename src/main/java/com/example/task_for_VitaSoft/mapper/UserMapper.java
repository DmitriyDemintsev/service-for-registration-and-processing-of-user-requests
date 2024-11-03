package com.example.task_for_VitaSoft.mapper;

import com.example.task_for_VitaSoft.dto.UserCreateDto;
import com.example.task_for_VitaSoft.dto.UserDto;
import com.example.task_for_VitaSoft.dto.UserForAdminDto;
import com.example.task_for_VitaSoft.dto.UserForOperatorDto;
import com.example.task_for_VitaSoft.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User toUser(UserCreateDto userCreateDto, Long id) {
        User user = new User(
                id,
                userCreateDto.getName(),
                userCreateDto.getEmail(),
                userCreateDto.getPassword(),
                userCreateDto.getRole()
        );
        return user;
    }

    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
        );
        return userDto;
    }

    public static List<UserForAdminDto> toUserDtoList(List<User> users) {
        List<UserForAdminDto> result = new ArrayList<>();
        for (User user : users) {
            result.add(toUserShortDto(user));
        }
        return result;
    }

    public static UserForAdminDto toUserShortDto(User user) {
        UserForAdminDto userForAdminDto = new UserForAdminDto(
                user.getName(),
                user.getEmail(),
                user.getRoles());
        return userForAdminDto;
    }

    public static UserForOperatorDto toUserForOperatorDto(User user) {
        UserForOperatorDto UserForOperatorDto = new UserForOperatorDto(
                user.getName());
        return UserForOperatorDto;
    }
}
