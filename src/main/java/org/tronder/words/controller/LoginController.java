package org.tronder.words.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.model.User;

@RestController
@RequestMapping("")
public class LoginController {

    @PostMapping("/login")
    @ResponseBody
    public User login(Authentication authentication) {
        return new User(authentication.getName());
    }

    @PostMapping("/logoutUser")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        response.setStatus(401);
    }

    // @RequestMapping(value = "/username", method = RequestMethod.GET)
    // @ResponseBody
    // public String currentUserName(Authentication authentication) {
    //     return authentication.getName();
    // }

}