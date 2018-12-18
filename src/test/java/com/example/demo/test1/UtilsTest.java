package com.example.demo.test1;

import com.example.demo.login.domain.User;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.shiro.cache.Cache;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.exception.SerializationException;
import org.crazycake.shiro.serializer.ObjectSerializer;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class UtilsTest {

    @Test
    public void test1() throws SerializationException {
        ObjectSerializer serializer = new ObjectSerializer();
        byte[] bytes = serializer.serialize(User.builder().id(1).name("zhong").password("123").enable("1").build());
        System.out.println(bytes.length);
        for (byte b : bytes){
            System.out.print(Integer.toHexString(0xff&b) + " ");
        }
        System.out.println();
        User user = (User) serializer.deserialize(bytes);
        System.out.println(user);
    }

    @Test
    public void test2(){
        RedisCacheManager manager = new RedisCacheManager();
        manager.setExpire(3600 * 24);
        manager.setRedisManager(new RedisManager());
        Cache<String, User> cache = manager.getCache("first");
        cache.put("name", User.builder().id(1).name("zhong").password("123").enable("1").build());
        User user = cache.get("name");
        System.out.println(user);
    }

    @Test
    public void test3() throws Exception {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(100);
        config.setMaxIdle(200);
        config.setMaxTotal(1000);
        GenericObjectPool<User> userObjectPool = new GenericObjectPool<>(new BasePooledObjectFactory<User>() {
            @Override
            public User create() throws Exception {
                return User.builder().id(1).name("zhong").password("123").enable("1").build();
            }

            @Override
            public PooledObject<User> wrap(User obj) {
                return new DefaultPooledObject<>(obj);
            }
        }, config);
        for (int i = 0; i < 1000000; i++){
            User user = null;
            try {
                user = userObjectPool.borrowObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("user:" + i + "," + user);
            System.out.println("active:" + userObjectPool.getNumActive());
            System.out.println("idle:" + userObjectPool.getNumIdle());
            System.out.println("wait:" + userObjectPool.getNumWaiters());
//            userObjectPool.returnObject(user);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<User> userQueue = new LinkedBlockingQueue<>(1000);
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0 ; i < 20; i++){
            service.execute(() -> {
                System.out.println(Thread.currentThread().getName());
                for (int i1 = 0; i1 < 100; i1++){
                    System.out.println("offer:" + i1);
                    userQueue.offer(User.builder().id(1).name("zhong").password("123").enable("1").build());
                }
            });
        }
        service.execute(()->{
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/test.txt"));
                ObjectSerializer serializer = new ObjectSerializer();
                while (true){
                    User user = userQueue.take();
                    bos.write(serializer.serialize(user));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
