package org.tronder.words.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.tronder.words.model.UserData;

import java.io.Serializable;

@Component
public class AuthenticationUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    public boolean isAuthenticated() {
        Authentication auth = getCurrentAuthentication();
        return auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser");
    }

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public void setCurrentAuthentication(Authentication auth) {
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public UserData getUserData() {
        return (UserData) getCurrentAuthentication().getPrincipal();
    }
}
