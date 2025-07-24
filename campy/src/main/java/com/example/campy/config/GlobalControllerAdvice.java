package com.example.campy.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    @ModelAttribute("loggedInUser")
    public String addLoggedInUserToModel(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            return authentication.getName();  // 유저 이름 또는 ID
        }

        return null; // 로그인 안한 경우
    }
}
