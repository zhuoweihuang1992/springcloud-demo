package com.alog.dataservice.auth.service;


import com.alog.dataservice.auth.entity.AuthResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alog.dataservice.auth.repository.RoleResourceRepository;

@Service
public class AuthResourceServiceImpl {
    @Autowired
    private RoleResourceRepository roleResourceRepository;

    public AuthResource loadResourceByUsername(String username, String role, String url){
        AuthResource resource = roleResourceRepository.findByUsername(username,role,url);
        return resource;
    }

}
