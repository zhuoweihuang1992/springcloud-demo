package com.alog.dataservice.backend.mapper;

import com.alog.dataservice.backend.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> getAllUsers(String dataCode);
    int addUser(User user);
    int deleteUser(User user);
}
