package com.example.demo.cglibtest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.lang.reflect.Method;

public class TransactionProxy {

    public Transaction newInstance(JedisPool jedisPool){
        Enhancer e = new Enhancer();
        e.setSuperclass(Jedis.class);
        e.setCallback(new MyMethodInterceptor(jedisPool));
        return (Transaction) e.create();
    }


    class MyMethodInterceptor implements MethodInterceptor {

        JedisPool pool;

        public MyMethodInterceptor(JedisPool pool){
            this.pool = pool;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            Object returnValue = proxy.invokeSuper(obj, args);
            if (method.getName().equals("exec")){
                pool.close();
            }
            return returnValue;
        }
    }

}
