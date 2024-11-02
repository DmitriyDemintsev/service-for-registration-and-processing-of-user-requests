package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.model.Application;
import com.example.task_for_VitaSoft.model.enums.Direction;
import com.example.task_for_VitaSoft.model.enums.Status;

import java.util.List;

public interface AppService {

    Application createApp(Long userId, Application application); //создать заявку - только юзер

    Application updateApp(Long userId, Application application); // обновить заявку если черновик - только юзер

    List<Application> getUserApplications(Long userId, Direction direction, int page); //просмотреть все созданные заявки с пагинацией - только юзер

    Application sendApp(Long userId, Application application); //отправить заявку на рассмотрение - только юзер


    Application getAppById(Long appId); //найти заявку по id

    Application changeStatusOfApp(Long userId, Long appId, Status status); //принять/отклонить заявку - только оператор

    List<Application> viewAllApplications(Long userId, Direction direction, int page); //посмотреть все заявки - только оператор

    List<Application> viewUsersSubmittedApplications(Long userId, String name, Direction direction, int page); //просмотреть заявки конкретного юзера
}
