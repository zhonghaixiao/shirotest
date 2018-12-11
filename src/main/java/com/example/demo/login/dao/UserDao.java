package com.example.demo.login.dao;

import com.example.demo.login.domain.Permission;
import com.example.demo.login.domain.Role;
import com.example.demo.login.domain.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Repository
@Mapper
public interface UserDao {

    User selectById(long id);

    User selectByName(String name);

    int register(User user) throws SQLIntegrityConstraintViolationException;

    List<Role> getRolesByUsername(String username);

    List<Permission> getPermissionsByRoleId(String roleId);

}
