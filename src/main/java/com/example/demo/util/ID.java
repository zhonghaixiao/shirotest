package com.example.demo.util;

import java.util.UUID;

public class ID {

    public static long next(){
        return Long.parseLong(UUID.randomUUID().toString().replaceAll("-",""));
    }

}
