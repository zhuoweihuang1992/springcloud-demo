package com.alog.dataservice.backend.controller;

import com.alog.dataservice.backend.entity.User;
import com.alog.dataservice.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/backend")
public class BackendController {

    @Autowired
    private UserService userService;

    @RequestMapping("/admin")
    public List<List<User>> admin(@RequestBody Map<String, String> codesMap) {
        User user = new User();
        user.setDataCode(codesMap.get("codes"));
        List<List<User>> users = userService.getAllUsers(user);
        return users;
    }

    @PostMapping("/user")
    public String user() {
        return "Hello User!";
    }

    @PostMapping("/guest")
    public String guest() {
        return "Hello Guest!";
    }
}

