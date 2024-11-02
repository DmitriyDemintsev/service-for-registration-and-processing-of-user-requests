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

    @Query("select u from User u where upper (name) like upper (concat('%', ?1, '%'))")
    List<User> findUserBySearch(String name);

    User findUserByEmail(String email);

    Optional<User> findByEmail(String email);
}
