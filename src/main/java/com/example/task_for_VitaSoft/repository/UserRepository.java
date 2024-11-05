package com.example.task_for_VitaSoft.repository;

import com.example.task_for_VitaSoft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    List<User> findByNameContainingIgnoreCase(String name);

    Optional<User> findByEmail(String email);
}
