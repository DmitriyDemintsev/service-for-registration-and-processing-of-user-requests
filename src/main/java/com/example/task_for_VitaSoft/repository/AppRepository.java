package com.example.task_for_VitaSoft.repository;

import com.example.task_for_VitaSoft.model.Application;
import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.model.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppRepository extends JpaRepository<Application, Long> {

    //найти заявки по appId
    Optional<Application> findById(Long appId);

    //найти все заявки пользователя
    List<Application> findAllByAuthor(User user, Pageable pageable);

    //найти заявки по статусу
    List<Application> findApplicationsByStatus(Status status, Pageable pageable);

    //найти заявки по юзеру и статусу
    List<Application> findApplicationsByAuthorAndStatus(User user, Status status, Pageable pageable);
}
