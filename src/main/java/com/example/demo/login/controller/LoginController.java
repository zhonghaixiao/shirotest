package com.example.demo.login.controller;

import com.example.demo.login.dao.UserDao;
import com.example.demo.login.domain.User;
import com.example.demo.login.service.LoginService;
import com.example.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.util.SnowflakeIdWorker.next;

@Controller
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    LoginService service;

    @PostMapping("register")
    public Result register(@RequestBody User user){
        user.setId(next());
        boolean isSuccess = service.register(user);
        System.out.println("isSuccess: " + isSuccess);
        return Result.ok(isSuccess);
    }

    @PostMapping("login")
    public Result login(@RequestBody User user){
        boolean isSuccess = service.login(user);
        System.out.println("isSuccess: " + isSuccess);
        return Result.ok(isSuccess);
    }

}
