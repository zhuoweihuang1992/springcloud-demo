package com.alog.dataservice.auth.controller;

import com.alog.dataservice.auth.entity.AuthResource;
import com.alog.dataservice.auth.entity.User;
import com.alog.dataservice.auth.service.AuthResourceServiceImpl;
import com.alog.dataservice.auth.service.TokenService;
import com.alog.dataservice.auth.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证token以及用户权限，数据范围服务
 */
@RestController
public class VerifyTokenController {

    @Value("{$dataservice.security.jwt.secret}")//token 秘钥
    private String secret;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthResourceServiceImpl authResourceService;


    @RequestMapping("/verifyToken")
    public String verifyToken(@RequestHeader("Authorization") String token,@RequestHeader("url") String url){
        if (token != null && token.startsWith("Bearer" + " ")) {
            token = token.replace("Bearer" + " ", "");
            try {
                //解析token
                Claims claims = Jwts.parser()
                        .setSigningKey(secret.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                //根据用户名查询redis中的token
                String redisToken = tokenService.findTokenByName(username);
                if(redisToken != null){
                    User user = new User();
                    //根据用户名查询该用户信息
                    User us = userService.findByUserName(username);
                    String role = us.getRole();
                    AuthResource authResource = new AuthResource();
                    try{
                        //可能存在多个角色的情况,多个的话以","分割
                        String[] roles = us.getRole().split(",");
                        for(int i=0;i<roles.length;){
                            role = roles[i];
                            System.out.println(role);
                            //查询已授权该用户角色的可允许访问url，是否存在正在访问的url
                            authResource = authResourceService.loadResourceByUsername(us.getUsername(),role, url);
                            if(authResource != null){
                                //匹配成功,得到数据范围编码，并返回
                                String data_area_code = authResource.getData_area_code();
                                return data_area_code;
                            }
                            i++;
                        }
                    }catch (Exception e){
                        System.out.println("单个角色");
                    }

                }else{
                    System.out.println("该token已过期，请重新申请！");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "1";
    }
}
