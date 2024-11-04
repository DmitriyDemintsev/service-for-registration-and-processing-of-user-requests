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
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RequiredArgsConstructor
@Service
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
                .orElseThrow(() -> new UserValidationException("The user does not exist"));
        log.debug("User with id {} ", userId + " is not in the database");
        if (application.getText() == null || application.getText().isBlank()) {
            log.debug("The application text is missing");
            throw new AppValidationException("The application text is missing");
        }
        if (user.getRoles().contains(USER)) {
            log.debug("Creating a request from a user with an id {}", userId);
            application.setAuthor(user);
            application.setStatus(DRAFT);
            application.setCreated(LocalDateTime.now());
            applicationRepository.save(application);
        } else {
            log.debug("A user with id {} ", userId + " does not have rights to create an application");
            throw new UserValidationException("There are no rights to create an application");
        }
        log.debug("The application with id = {} has been created", application.getAppId());
        return application;
    }

    /**
     * пользователь может просматривать созданные им заявки с сортировкой
     * по дате ASC/DESC и пагинацией по 5 элементов
     */
    @Override
    public List<Application> getUserApplications(Long userId, Direction direction, int page) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("The user does not exist"));
        log.debug("User with id {} ", userId + " is not in the database");
        if (direction.equals(DECREASING)) {
            log.debug("ПA user with id {} ", userId + " received applications for viewing in descending order");
            return applicationRepository.findAllByAuthor(user, getPageableDesc(page));
        }
        log.debug("The user with the id {} ", userId + " received applications for viewing in ascending order");
        return applicationRepository.findAllByAuthor(user, getPageableAsc(page));
    }

    /**
     * пользователь может редактировать созданные им заявки в статусе "черновик"
     */
    @Override
    public Application updateApp(Long userId, Application application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("The user does not exist"));
        log.debug("User with id {} ", userId + " is not in the database");
        Application old = applicationRepository.findById(application.getAppId())
                .orElseThrow(() -> new ApplicationNotFoundException("The application was not found"));
        if (!old.getAuthor().getUserId().equals(userId)) {
            log.debug("The user with id {} ", userId + " does not have rights to edit the application");
            throw new UserValidationException("Only the author can edit the application");
        }
        if (application.getText().isBlank()) {
            log.debug("The user with the id {} ", userId + " did not send the text to change the request");
            throw new AppValidationException("The application text is missing");
        }
        if (user.getRoles().contains(USER) && old.getStatus().equals(DRAFT)) {
            log.debug("Changing the status of the application");
            application.setAuthor(old.getAuthor());
            application.setText(application.getText());
            application.setStatus(old.getStatus());
            application.setCreated(old.getCreated());
        } else {
            log.debug("The application of a user with id {} ", userId + " is not available for modification");
            throw new AppValidationException("The application is not available for editing");
        }
        log.debug("A user with id {} ", userId + " has changed the application text");
        return applicationRepository.save(application);
    }

    /**
     * пользователь может отправлять заявки на рассмотрение оператору
     */
    @Override
    public Application sendApp(Long userId, Application application) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("The user does not exist"));
        log.debug("User with id {} ", userId + " is not in the database");
        Application old = applicationRepository.findById(application.getAppId())
                .orElseThrow(() -> new ApplicationNotFoundException("The application was not found"));
        if (user.getRoles().contains(USER) && application.getAuthor().getUserId().equals(userId)) {
            log.debug("A user with id {} ", userId + " sends an application for consideration to the operator");
            application.setAuthor(old.getAuthor());
            application.setText(old.getText());
            application.setStatus(Status.SENT);
            application.setCreated(old.getCreated());
        } else {
            log.debug("The user with id {} ", userId
                    + " does not have the rights to send an application for consideration to the operator");
            throw new UserValidationException("You cannot submit an application for review");
        }
        log.debug("A user with id {} ", userId + " has sent an application for consideration to the operator");
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
        if (!userService.getUserById(userId).getRoles().contains(OPERATOR)) {
            log.debug("A user with id {} ", userId + " does not have rights to view applications");
            throw new UserValidationException("You do not have rights to view applications");
        }

        List<Application> applications = new ArrayList<>();
        List<Application> old = applicationRepository.findAll();
        for (Application application : old) {
            String text = application.getText();
            StringBuilder newText = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                newText.append(i != text.length() - 1 ? character + "-" : character);
            }
            application.setText(String.valueOf(newText));
        }

        if (direction.equals(INCREASING)) {
            log.debug("A user with id {} ", userId + " has received applications for viewing in ascending order");
            applications.addAll(applicationRepository.findApplicationsByStatus(SENT, getPageableAsc(page)));
        } else {
            log.debug("A user with id {} ", userId + " received applications for viewing in descending order");
            applications.addAll(applicationRepository.findApplicationsByStatus(SENT, getPageableDesc(page)));
        }
        log.debug("The user with the id {} ", userId + " has been provided with applications for viewing");
        return applications;
    }

    /**
     * оператор может просматривать отправленные заявки только конкретного пользователя по его имени/части имени
     * с сортировкой по дате ASC/DESC и пагинацией по 5 элементов
     */
    @Override
    public List<Application> viewUsersSubmittedApplications(Long userId, String name, Direction direction, int page) {
        if (!userService.getUserById(userId).getRoles().contains(OPERATOR)) {
            log.debug("A user with id {} ", userId + " does not have rights to view applications");
            throw new UserValidationException("You do not have rights to view applications");
        }
        List<User> users = userService.getUserByNameSearch(name);
        List<Application> applications = new ArrayList<>();
        for (User user : users) {
            List<Application> old = applicationRepository.findAllByAuthor(user);
            for (Application application : old) {
                String text = application.getText();
                StringBuilder newText = new StringBuilder();
                for (int i = 0; i < text.length(); i++) {
                    char character = text.charAt(i);
                    newText.append(i != text.length() - 1 ? character + "-" : character);
                }
                application.setText(String.valueOf(newText));
            }
            if (direction.equals(INCREASING)) {
                log.debug("A user with id {} ", userId + " uploads the applications of a user with the name " + name
                        + " for viewing in ascending order");
                applications.addAll(applicationRepository.findApplicationsByAuthorAndStatus(user,
                        SENT, getPageableAsc(page)));
            } else {
                log.debug("A user with id {} ", userId + " uploads the applications of a user with the name " + name
                        + " for viewing in descending order");
                applications.addAll(applicationRepository.findApplicationsByAuthorAndStatus(user,
                        SENT, getPageableDesc(page)));
            }
        }
        log.debug("For a user with id {} ", userId + ", the applications of a user with the name " + name
                + " have been uploaded for viewing");
        return applications;
    }

    /**
     * оператор может принимать и отклонять заявки
     */
    @Override
    public Application changeStatusOfApp(Long userId, Long appId, Status status) {
        Application application = applicationRepository.findById(appId)
                .orElseThrow(() -> new ApplicationNotFoundException("The application was not found"));
        log.debug("Application with id {} ", appId + " not found");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserValidationException("Такого пользователя не существует"));
        log.debug("User with id {} ", userId + " is not in the database");
        if (user.getRoles().contains(OPERATOR) && application.getStatus().equals(SENT)) {
            if (status.equals(ACCEPTED)) {
                log.debug("Changing the status of the user's request {} ", user.getName() + " to ACCEPTED");
                application.setStatus(ACCEPTED);
            } else if (status.equals(REJECTED)) {
                log.debug("Changing the status of the user's request {} ", user.getName() + " to REJECTED");
                application.setStatus(REJECTED);
            }
        } else {
            log.debug("The user with id {} ", user.getName() +
                    " does not have the rights to change the status of the application");
            throw new UserValidationException("У вас нет прав на работу заявками");
        }
        log.debug("The status of the application with id {} ", appId + " has been changed");
        return applicationRepository.save(application);
    }

    @Override
    public Application getAppById(Long appId) {
        log.debug("Receiving an application by its id");
        return applicationRepository.findById(appId).orElseThrow();
    }
}
