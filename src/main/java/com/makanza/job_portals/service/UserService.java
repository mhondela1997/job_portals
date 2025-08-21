package com.makanza.job_portals.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.makanza.job_portals.model.MyUser;
import com.makanza.job_portals.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Find user by username
     */
    public MyUser findByUsername(String username) {
        Optional<MyUser> userOpt = userRepository.findByUsername(username);
        return userOpt.orElse(null);
    }

    /**
     * Find user by ID
     */
    public MyUser findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Save or update a user
     */
    public MyUser save(MyUser user) {
        return userRepository.save(user);
    }
}

