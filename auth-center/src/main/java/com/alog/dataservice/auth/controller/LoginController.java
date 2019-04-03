package com.alog.dataservice.auth.controller;


import com.alog.dataservice.auth.config.RedisService;
import com.alog.dataservice.auth.entity.User;
import com.alog.dataservice.auth.entity.UserToken;
import com.alog.dataservice.auth.service.TokenService;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alog.dataservice.auth.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 登录服务
 */
@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Resource
    private ValueOperations<String, Object> valueOperations;

    @Resource
    private RedisService redisService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody Map<String, String> loginUser,HttpServletResponse response) {
        User user = new User();
        user.setUsername(loginUser.get("username"));
        //查询该用户
        User us = userService.findByUserName(user.getUsername());
        if(us != null){
            //校验登录
            boolean pswFlag = BCrypt.checkpw(loginUser.get("password"),us.getPassword());
            if(pswFlag){
                user.setUsername(us.getUsername());
                user.setPassword(loginUser.get("password"));
                String token = null;
                try{
                    //查询redis是否存在该token
                    token = tokenService.findTokenByName(us.getUsername());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(token == null){
                    //redis不存的话，则生成token
                    token = tokenService.getToken(user);
                    //将token保存到redis并设置有效时间
                    UserToken userToken = new UserToken();
                    userToken.setUsername(loginUser.get("username"));
                    userToken.setToken(token);
                    valueOperations.set(loginUser.get("username"),userToken);
                    redisService.expireKey(loginUser.get("username"),60*60*24, TimeUnit.SECONDS);
                }
                //在响应头添加token,响应回去
                response.addHeader("Authorization","Bearer" + " " + token);
                return "登录成功！";
            }
        }
        return "登录失败！";
    }

}