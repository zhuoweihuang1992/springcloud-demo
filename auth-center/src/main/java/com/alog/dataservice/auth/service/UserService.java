package com.alog.dataservice.auth.service;

import com.alog.dataservice.auth.entity.User;

public interface UserService {
    User findByUserName(String username);
}
