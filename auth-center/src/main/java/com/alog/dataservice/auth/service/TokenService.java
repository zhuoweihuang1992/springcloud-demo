package com.alog.dataservice.auth.service;

import com.alog.dataservice.auth.entity.User;

public interface TokenService {
    String getToken(User user);
    String findTokenByName(String name);
}

