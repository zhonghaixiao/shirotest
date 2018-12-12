package com.example.demo.config;

import com.example.demo.cglibtest.JedisProxy;
import com.example.demo.util.Result;
import lombok.val;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import javax.annotation.Resource;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@ComponentScan("com.example.demo")
@PropertySource("classpath:props/jedis.properties")
public class SpringConfig {

    @Value("${host}")
    private String redisHost;

    @Value("${port}")
    private int port;

    @Value("${timeout}")
    private int timeOut;

    @Value("${database}")
    private int database;

    @Value("${maxClients}")
    private int maxClients;

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setTestOnBorrow(true);
        config.setMinIdle(8);
        return new JedisPool(config, redisHost, port, timeOut,null, database);
    }

    @Bean
    public Jedis createJedisProxy(){
        return new JedisProxy().newInstance(jedisPool());
    }

    @ExceptionHandler(ShiroException.class)
    public ResponseEntity handleShiroException(ShiroException e){
        System.out.println("shiro exception");
        System.out.println(e.getClass());
        if (e.getClass().isAssignableFrom(IncorrectCredentialsException.class)) {
            System.out.println("exception handler, catch incorrect credential exception");
            return new ResponseEntity<>(Result.fail("incorrect credential", false), HttpStatus.OK);
        } else if (e.getClass().isAssignableFrom(UnauthenticatedException.class)){
            System.out.println("exception handler, catch UnauthenticatedException");
            return new ResponseEntity<>(Result.fail("UnauthenticatedException"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Result.fail("unknown shiro error", false), HttpStatus.OK);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity checkException(RuntimeException e) throws Throwable {
        System.out.println(e.getCause().getClass());
        if (e.getCause().getClass().isAssignableFrom(SQLIntegrityConstraintViolationException.class)){
            System.out.println("exception handler, catch duplicate name exception");
            return new ResponseEntity<>(Result.fail("duplicate name", false), HttpStatus.OK);
        } else if (ShiroException.class.isAssignableFrom(e.getCause().getClass())){
            throw e.getCause();
        }
        return new ResponseEntity<>(Result.fail("unknown error", false), HttpStatus.OK);
    }

}
