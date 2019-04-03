package com.alog.dataservice.auth.service.impl;

import com.alog.dataservice.auth.entity.User;
import com.alog.dataservice.auth.repository.UserRepository;
import com.alog.dataservice.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;


    public User findByUserName(String username) {
        User user = userRepository.findByUsername(username);
        return user;

    }
}
