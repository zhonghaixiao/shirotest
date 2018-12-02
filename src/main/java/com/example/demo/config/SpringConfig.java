package com.example.demo.config;

import com.example.demo.util.Result;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class SpringConfig {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity checkException(RuntimeException e){
        if (e.getCause().getClass().isAssignableFrom(SQLIntegrityConstraintViolationException.class)){
            System.out.println("exception handler, catch duplicate name exception");
            return new ResponseEntity<>(Result.fail("duplicate name", false), HttpStatus.OK);
        } else if (e.getCause().getClass().isAssignableFrom(IncorrectCredentialsException.class)) {
            System.out.println("exception handler, catch incorrect credential exception");
            return new ResponseEntity<>(Result.fail("incorrect credential", false), HttpStatus.OK);
        }
        return new ResponseEntity<>(Result.fail("unknown error", false), HttpStatus.OK);
    }

}
