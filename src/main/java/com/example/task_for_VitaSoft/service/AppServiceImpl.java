package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.exception.AppValidationException;
import com.example.task_for_VitaSoft.exception.ApplicationNotFoundException;
import com.example.task_for_VitaSoft.exception.UserValidationException;
import com.example.task_for_VitaSoft.model.Application;
import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.model.enums.Direction;
import com.example.task_for_VitaSoft.model.enums.Status;
import com.example.task_for_VitaSoft.repository.AppRepository;
import com.example.task_for_VitaSoft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.task_for_VitaSoft.model.enums.Direction.DECREASING;
import static com.example.task_for_VitaSoft.model.enums.Direction.INCREASING;
import static com.example.task_for_VitaSoft.model.enums.Role.OPERATOR;
import static com.example.task_for_VitaSoft.model.enums.Role.USER;
import static com.example.task_for_VitaSoft.model.enums.Status.*;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppRepository applicationRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private static final int PAGE_SIZE = 5;

    /**
     * пользователь может создавать заявки
     */
    @Override
    public Application createApp(Long userId, Application application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        if (application.getText() == null || application.getText().isBlank()) {
            throw new AppValidationException("Отсутствует текст заявки");
        }
        if (user.getRoles().contains(USER)) {
            application.setAuthor(user);
            application.setStatus(DRAFT);
            application.setCreated(LocalDateTime.now());
            application = applicationRepository.save(application);
        } else {
            throw new UserValidationException("У вас нет прав на создание заявки");
        }
        return application;
    }

    /**
     * пользователь может просматривать созданные им заявки с сортировкой
     * по дате ASC/DESC и пагинацией по 5 элементов
     */
    @Override
    public List<Application> getUserApplications(Long userId, Direction direction, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        if (direction.equals(DECREASING)) {
            return applicationRepository.findAllByAuthor(user, getPageableDesc(page));
        }
        return applicationRepository.findAllByAuthor(user, getPageableAsc(page));
    }

    /**
     * пользователь может редактировать созданные им заявки в статусе "черновик"
     */
    @Override
    public Application updateApp(Long userId, Application application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        Application old = applicationRepository.findById(application.getAppId())
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));
        if (!old.getAuthor().getUserId().equals(userId)) {
            throw new UserValidationException("Редактировать заявку может только автор");
        }
        if (application.getText().isBlank()) {
            throw new AppValidationException("Отсутствует текст заявки");
        }
        if (user.getRoles().contains(USER) && old.getStatus().equals(DRAFT)) {
            application.setAuthor(old.getAuthor());
            application.setText(application.getText());
            application.setStatus(old.getStatus());
            application.setCreated(old.getCreated());
        } else {
            throw new AppValidationException("Заявка недоступна для редактирования");
        }
        return applicationRepository.save(application); //тут
    }

    /**
     * пользователь может отправлять заявки на рассмотрение оператору
     */
    @Override
    public Application sendApp(Long userId, Application application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        Application old = applicationRepository.findById(application.getAppId())
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));
        if (user.getRoles().contains(USER) && application.getAuthor().getUserId().equals(userId)) {
            application.setAuthor(old.getAuthor());
            application.setText(old.getText());
            application.setStatus(Status.SENT);
            application.setCreated(old.getCreated());
        } else {
            throw new UserValidationException("Вы не можете отправить заявку на расмотрение");
        }
        return applicationRepository.save(application);
    }

    public static Pageable getPageableDesc(int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    public static Pageable getPageableAsc(int page) {
        Sort sort = Sort.by(Sort.Direction.ASC, "created");
        return PageRequest.of(page, PAGE_SIZE, sort);
    }

    /**
     * оператор может просматривать все отправленные на рассмотрение заявки
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @Override
    public List<Application> viewAllApplications(Long userId, Direction direction, int page) {
        List<Application> applications = new ArrayList<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        if (!user.getRoles().contains(OPERATOR)) {
            throw new UserValidationException("У вас нет прав на просмотр заявок");
        }
        if (direction.equals(INCREASING)) {
            applications.addAll(applicationRepository.findApplicationsByStatus(SENT, getPageableAsc(page)));
        } else {
            applications.addAll(applicationRepository.findApplicationsByStatus(SENT, getPageableDesc(page)));
        }
        return applications;
    }

    /**
     * оператор может просматривать отправленные заявки только конкретного пользователя по его имени/части имени
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @Override
    public List<Application> viewUsersSubmittedApplications(Long userId, String name, Direction direction, int page) {
        List<User> users = userService.getUserByNameSearch(name);
        List<Application> applications = new ArrayList<>();
        if (!userService.getUserById(userId).getRoles().contains(OPERATOR)) {
            throw new UserValidationException("У вас нет прав на просмотр заявок");
        }
        for (User user : users) {
            if (direction.equals(INCREASING)) {
                applications.addAll(applicationRepository.findApplicationsByAuthorAndStatus(user, SENT, getPageableAsc(page)));
            } else {
                applications.addAll(applicationRepository.findApplicationsByAuthorAndStatus(user, SENT, getPageableDesc(page)));
            }
        }
        return applications;
    }

    /**
     * оператор может принимать и отклонять заявки
     */
    @Override
    public Application changeStatusOfApp(Long userId, Long appId, Status status) {
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        if (user.getRoles().contains(OPERATOR) && application.getStatus().equals(SENT)) {
            if (status.equals(ACCEPTED)) {
                application.setStatus(ACCEPTED);
            } else if (status.equals(REJECTED)) {
                application.setStatus(Status.REJECTED);
            }
        } else {
            throw new UserValidationException("У вас нет прав на работу заявками");
        }
        return applicationRepository.save(application);
    }

    //найти заявку по её id
    @Override
    public Application getAppById(Long appId) {
        return applicationRepository.findById(appId).orElseThrow();
    }
}
