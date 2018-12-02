package com.example.demo.config;

import com.example.demo.login.domain.User;
import com.example.demo.login.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Autowired
    LoginService service;

    @Bean
    public SecurityManager securityManager(){
        SecurityManager securityManager = new DefaultSecurityManager(myRealm());
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean
    public Realm myRealm(){
        return new AuthenticatingRealm() {
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
                String username = (String) token.getPrincipal();
                User user = service.getUserByName(username);
                //数据库操作，取出密码userToken
                //返回    SimpleAuthenticationInfo    用于校验
                return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
            }
        };
    }

}
