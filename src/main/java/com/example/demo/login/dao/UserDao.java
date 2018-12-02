package com.example.demo.login.dao;

import com.example.demo.login.domain.User;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;

@Repository
@Mapper
public interface UserDao {

    User selectById(long id);

    User selectByName(String name);

    int register(User user) throws SQLIntegrityConstraintViolationException;

}
