package com.example.demo.cglibtest;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Method;

public class JedisProxy {

    public Jedis newInstance(JedisPool jedisPool){
        Enhancer e = new Enhancer();
        e.setSuperclass(Jedis.class);
        e.setCallback(new MyMethodInterceptor(jedisPool));
        return (Jedis) e.create();
    }

    class MyMethodInterceptor implements MethodInterceptor{

        JedisPool pool;

        public MyMethodInterceptor(JedisPool pool){
            this.pool = pool;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            Jedis jedis = pool.getResource();
            Object returnValue = proxy.invokeSuper(obj, args);
            switch (method.getName()){
                case "multi":
                    final Jedis finalPooled = jedis;
                    return new TransactionProxy().newInstance(pool);
                case "close":
                    pool.destroy();
                    break;
                default:
                    jedis.close();
            }
            return returnValue;
        }
    }

}
