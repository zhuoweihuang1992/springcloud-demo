package com.alog.dataservice.auth.controller;

import com.alog.dataservice.auth.entity.User;
import com.alog.dataservice.auth.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody Map<String,String> userinfo){
        try{
            //查询该用户是否已存在
            User user = userRepository.findByUsername(userinfo.get("username"));
            if(user != null){
                return "该用户已存在！";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String uuid = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());
        User user = new User();
        user.setUsername(userinfo.get("username"));
        String pwt = BCrypt.hashpw(userinfo.get("password"), BCrypt.gensalt());
        System.out.println("加密后的密码："+pwt);
        user.setPassword(pwt);
        user.setRole("ROLE_USER");
        user.setAppid(uuid);
        userRepository.save(user);
        return "注册成功！";
    }
}
