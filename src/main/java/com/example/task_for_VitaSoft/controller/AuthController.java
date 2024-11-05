package com.example.task_for_VitaSoft.controller;

import com.example.task_for_VitaSoft.dto.UserCreateDto;
import com.example.task_for_VitaSoft.dto.UserDto;
import com.example.task_for_VitaSoft.mapper.UserMapper;
import com.example.task_for_VitaSoft.security.UserDetailsImpl;
import com.example.task_for_VitaSoft.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Component
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/myself")
    public String getMyself() {
        final var authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.info("Authentication is null");
            return null;
        }
        final var principal = authentication.getPrincipal();
        if (principal == null) {
            log.info("Principal is null");
            return null;
        }
        if (!(principal instanceof UserDetailsImpl)) {
            log.info("Principal is not UserDetailsImpl, but {}", principal);
            return null;
        }
        final var user = ((UserDetailsImpl) principal).getUser();
        log.info("Got user: {}", user);
        return user.getEmail();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        log.debug("The user logs out");
        return "You are logged out of the system";
    }
}
