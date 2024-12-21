package me.project.backend.util;

import lombok.extern.slf4j.Slf4j;
import me.project.backend.payload.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class ContextUtil {

    static public Optional<String> getUsername() {
        UserDetailsImpl principal;
        try {
            principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (RuntimeException e) {
            log.error("Cannot get username: {}", e.getMessage());
            return Optional.empty();
        }
        log.debug("principal: {}", principal);
        return Optional.of(principal.getUsername());
    }
}
