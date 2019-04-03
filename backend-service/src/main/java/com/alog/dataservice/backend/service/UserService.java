package com.alog.dataservice.backend.service;

import com.alog.dataservice.backend.entity.User;

import java.util.List;

public interface UserService {
    public List<List<User>> getAllUsers(User user);
    public int addUser(User user);
    public int deleteUser(User user);

}
