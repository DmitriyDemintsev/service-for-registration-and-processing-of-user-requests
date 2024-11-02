package com.example.task_for_VitaSoft.service;

import com.example.task_for_VitaSoft.model.User;
import com.example.task_for_VitaSoft.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    // todo: throw new UnauthorizedException
    @Override
    public User getCurrentUser() {
        final var authentication =  SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            log.debug("Authentication is null");
            throw new IllegalStateException("TODO");
        }
        final var principal = authentication.getPrincipal();
        if (principal == null) {
            log.debug("Principal is null");
            throw new IllegalStateException("TODO");
        }
        if (!(principal instanceof UserDetailsImpl)) {
            log.debug("Principal is not UserDetailsImpl, but {}", principal);
            throw new IllegalStateException("TODO");
        }
        final var user = ((UserDetailsImpl) principal).getUser();
        log.debug("Got user: {}", user.getEmail());
        return user;
    }
}
