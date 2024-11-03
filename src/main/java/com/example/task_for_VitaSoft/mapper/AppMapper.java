package com.example.task_for_VitaSoft.mapper;

import com.example.task_for_VitaSoft.dto.AppCreateDto;
import com.example.task_for_VitaSoft.dto.AppDto;
import com.example.task_for_VitaSoft.dto.AppUpdateDto;
import com.example.task_for_VitaSoft.exception.AppValidationException;
import com.example.task_for_VitaSoft.model.Application;
import com.example.task_for_VitaSoft.model.enums.Direction;
import com.example.task_for_VitaSoft.model.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppMapper {

    public static Application toCreateApp(AppCreateDto appCreateDto) {
        return new Application(
                null,
                null,
                appCreateDto.getText(),
                null,
                LocalDateTime.now()
        );
    }

    public static Application toUpdateApp(AppUpdateDto appUpdateDto, long appId) {
        return new Application(
                appId,
                null,
                appUpdateDto.getText(),
                null,
                null
        );
    }

    public static AppDto toApplicationsDto(Application application) {
        return new AppDto(
                application.getAppId(),
                UserMapper.toUserShortDto(application.getAuthor()),
                application.getText(),
                application.getStatus(),
                application.getCreated()
        );
    }

    public static Direction toDirect(String direct) {
        try {
            return Direction.valueOf(direct);
        } catch (IllegalArgumentException e) {
            throw new AppValidationException("Unknown state: " + direct);
        }
    }

    public static Status toStatus(String status) {
        try {
            return Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new AppValidationException("Unknown state: " + status);
        }
    }

    public static List<AppDto> toAppDtoList(Iterable<Application> applications) {
        List<AppDto> result = new ArrayList<>();
        for (Application application : applications) {
            result.add(toApplicationsDto(application));
        }
        return result;
    }
}
