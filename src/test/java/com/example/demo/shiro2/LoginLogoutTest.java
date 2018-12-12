package com.example.demo.shiro2;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class LoginLogoutTest {

    @Test
    public void testHelloworld() {
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro/shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
        try{
            subject.login(token);
        }catch (AuthenticationException e){
            System.out.println("身份验证失败");
        }
        assertEquals(true, subject.isAuthenticated());
        subject.logout();

    }

    @Test
    public void multiRealms(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealms(Arrays.asList(new MyRealm1(), new MyRealm2(),new MyRealm3(), new MyRealm4()));
    }

}
