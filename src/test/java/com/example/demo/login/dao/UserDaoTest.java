package com.example.demo.login.dao;

import com.example.demo.login.domain.Permission;
import com.example.demo.login.domain.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {

    @Autowired
    UserDao dao;

    @Test
    public void test(){
        List<Role> roles = dao.getRolesByUsername("zhong");
        System.out.println(roles);
        Set<String> simpleRoles = roles.stream().map(Role::getDescription).collect(Collectors.toSet());
        System.out.println(simpleRoles);
        Set<String> simplePermissions = roles.stream().flatMap(role -> role.getPermissions().stream().map(Permission::getName)).collect(Collectors.toSet());
        System.out.println(simplePermissions);
    }

}