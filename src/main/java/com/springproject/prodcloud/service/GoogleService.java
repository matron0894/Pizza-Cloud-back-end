package com.springproject.prodcloud.service;

import com.springproject.prodcloud.domain.Provider;
import com.springproject.prodcloud.domain.Role;
import com.springproject.prodcloud.domain.User;
import com.springproject.prodcloud.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleService {

    private final UserRepository repo;

    public GoogleService(UserRepository repo) {
        this.repo = repo;
    }

    public void processOAuthPostLogin(String fullname, String username) {
        User existUser = repo.findByUsername(username);

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFullname(fullname);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setRoles(Collections.singleton(Role.USER));

            repo.save(newUser);
        }

    }
}
