package com.example.demo.login.service;

import com.example.demo.login.dao.UserDao;
import com.example.demo.login.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class LoginService {

    @Autowired
    UserDao userDao;

    public boolean login(User user){
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
            try{
                subject.login(token);
                return true;
            }catch (AuthorizationException e){
                throw new RuntimeException(e);
            }catch (AuthenticationException e){
                System.out.println("login failed");
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public boolean register(User user){
        try{
            int count = userDao.register(user);
            if (count == 1){
                return true;
            }
        }catch (SQLIntegrityConstraintViolationException e){
            System.out.println("duplicate name");
            throw new RuntimeException(e);
        }
        return false;
    }

    public User getUserByName(String username) {
        return userDao.selectByName(username);
    }

}
