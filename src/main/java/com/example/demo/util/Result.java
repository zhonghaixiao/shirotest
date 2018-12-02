package com.example.demo.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private static final String RSPCODE_SUCCESS = "0";
    private static final String RSPCODE_FAILED = "1";

    private String code, message;
    private Object data;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public static Result fail(String msg) {
        return new Result(RSPCODE_FAILED, msg);
    }

    public static Result fail(String msg, Object object) {
        return new Result(RSPCODE_FAILED, msg, object);
    }

    public static Result fail(Object object) {
        return new Result(RSPCODE_FAILED, "请求失败", object);
    }

    public static Result ok(String msg) {
        return new Result(RSPCODE_SUCCESS, msg);
    }

    public static Result ok(String msg, Object object) {
        return new Result(RSPCODE_SUCCESS, msg, object);
    }

    public static Result ok(Object object) {
        return new Result(RSPCODE_SUCCESS, "请求成功", object);
    }

}