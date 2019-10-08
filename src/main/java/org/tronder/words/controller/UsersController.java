package org.tronder.words.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tronder.words.model.User;
import org.tronder.words.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private UserRepository repository;

    @GetMapping("")
    public Iterable<User> getUser() {
        return repository.findAll();
    }

    @PostMapping("")
    public User addUser(@RequestBody @Valid User user) {
        return user;
    }

}
