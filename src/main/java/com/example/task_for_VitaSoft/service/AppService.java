package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.model.Application;
import com.example.task_for_VitaSoft.model.enums.Direction;
import com.example.task_for_VitaSoft.model.enums.Status;

import java.util.List;

public interface AppService {

    Application createApp(Long userId, Application application);

    Application updateApp(Long userId, Application application);

    List<Application> getUserApplications(Long userId, Direction direction, int page);

    Application sendApp(Long userId, Application application);

    Application getAppById(Long appId);

    Application changeStatusOfApp(Long userId, Long appId, Status status);

    List<Application> viewAllApplications(Long userId, Direction direction, int page);

    List<Application> viewUsersSubmittedApplications(Long userId, String name, Direction direction, int page);
}
