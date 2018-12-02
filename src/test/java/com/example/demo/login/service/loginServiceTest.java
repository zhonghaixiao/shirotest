package com.example.demo.login.service;

import com.example.demo.login.dao.UserDao;
import com.example.demo.login.domain.User;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class loginServiceTest {

    @Autowired
    LoginService service;

    @Autowired
    UserDao userDao;

    @Test
    public void login() {
//        boolean success = service.login("root", "123");
//        assertTrue(success);
    }

    @Test
    public void eqltest(){
        User user = userDao.selectById(1);
        System.out.println(user);
    }

}