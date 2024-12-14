package me.project.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class Cookies {
    static public Optional<String> getCookie(HttpServletRequest request, String name){
        if (request == null) return Optional.empty();
        if (request.getCookies() == null) return Optional.empty();
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name)) return Optional.of(cookie.getValue());
        }
        return Optional.empty();
    }
}
