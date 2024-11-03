package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.exception.UnauthorizedException;
import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public User getCurrentUser() {
        final var authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.debug("Authentication is null");
            throw new UnauthorizedException("An attempt to log in to the application without authorization");
        }
        final var principal = authentication.getPrincipal();
        if (principal == null) {
            log.debug("Principal is null");
            throw new UnauthorizedException("An attempt to log in to the application without authorization");
        }
        if (!(principal instanceof UserDetailsImpl)) {
            log.debug("Principal is not UserDetailsImpl, but {}", principal);
            throw new UnauthorizedException("An attempt to log in to the application without authorization");
        }
        final var user = ((UserDetailsImpl) principal).getUser();
        log.debug("Got user: {}", user.getEmail());
        return user;
    }
}
