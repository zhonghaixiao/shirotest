package com.example.demo.login.controller;

import com.example.demo.login.dao.UserDao;
import com.example.demo.login.domain.User;
import com.example.demo.login.service.LoginService;
import com.example.demo.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("test-read")
    @RequiresPermissions("read")
    public  Result test(){
        return Result.ok("test read");
    }

}
