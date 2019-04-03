package com.alog.dataservice.backend.service.impl;

import com.alog.dataservice.backend.entity.User;
import com.alog.dataservice.backend.mapper.UserMapper;
import com.alog.dataservice.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Primary
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<List<User>> getAllUsers(User user) {
        String dataCode = user.getDataCode();
        String[] codes = dataCode.split(",");
        List<List<User>> list = new ArrayList<>();
        for(int i=0;i<codes.length;i++){
            list.add(userMapper.getAllUsers(codes[i]));
        }

        return list;
    }

    @Override
    public int addUser(User user) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        user.setCreatedTime( form.format(new Date()) );
        return userMapper.addUser( user );
    }

    @Override
    public int deleteUser(User user) {
        return userMapper.deleteUser( user );
    }

}
