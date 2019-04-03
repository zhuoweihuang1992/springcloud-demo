package com.alog.dataservice.auth.service.impl;

import com.alog.dataservice.auth.entity.User;
import com.alog.dataservice.auth.entity.UserToken;
import com.alog.dataservice.auth.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import com.alog.dataservice.auth.config.RedisService;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("{$dataservice.security.jwt.secret}")
    private String secret;

    @Resource
    private ValueOperations<String,Object> valueOperations;

    @Resource
    private RedisService redisService;


    @Override
    public String getToken(User user) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(Date.from(now))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();

        return token;
    }


    public String findTokenByName(String username) {
        UserToken userToken = (UserToken) valueOperations.get(username);
        return userToken.getToken();
    }

}
