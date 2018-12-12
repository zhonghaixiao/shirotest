package com.example.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.demo.login.domain.User;
import com.example.demo.login.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AllSuccessfulStrategy;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ShiroConfig {

    @Autowired
    LoginService service;

    @Bean
    public SecurityManager securityManager(){
        DefaultSecurityManager securityManager = new DefaultSecurityManager(myRealm());

        //设置authenticator
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

        //设置authorizer
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setPermissionResolver(new WildcardPermissionResolver());
        securityManager.setAuthorizer(authorizer);


        DruidDataSource source = new DruidDataSource();
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setUsername("root");
        source.setPassword("");

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(source);
        jdbcRealm.setPermissionsLookupEnabled(true);
        securityManager.setRealms(Arrays.asList(jdbcRealm));

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
