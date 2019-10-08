package org.tronder.words.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tronder.words.model.User;
import org.tronder.words.principals.UserPrincipal;
import org.tronder.words.repository.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findById(username);
        System.out.println(username);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Username not found");
        }
        System.out.println("ok");
        return new UserPrincipal(user.get());
    }

    public void createUser(User user) {
        repository.save(user);
    }

}
