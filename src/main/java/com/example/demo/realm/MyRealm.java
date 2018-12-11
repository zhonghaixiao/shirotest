package com.example.demo.realm;

import com.example.demo.login.dao.UserDao;
import com.example.demo.login.domain.Permission;
import com.example.demo.login.domain.Role;
import com.example.demo.login.domain.User;
import com.example.demo.login.service.LoginService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class MyRealm extends AuthorizingRealm {

    @Autowired
    private LoginService service;

    @Autowired
    UserDao userDao;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = service.getUserByName(username);
        //数据库操作，取出密码userToken
        //返回    SimpleAuthenticationInfo    用于校验
        return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String name = (String) principals.getPrimaryPrincipal();
        List<Role> roles = userDao.getRolesByUsername(name);
//        Set<SimpleRole> simpleRoles = roles.stream().map(r -> {
//            SimpleRole role = new SimpleRole();
//            role.setName(r.getDescription());
//            role.setPermissions(r.getPermissions().stream().map(
//                    p-> new WildcardPermission(p.getName())).collect(Collectors.toSet()));
//            return role;
//        }).collect(Collectors.toSet());
        Set<String> simpleRoles = roles.stream().map(Role::getDescription).collect(Collectors.toSet());
        Set<String> simplePermissions = roles.stream()
                .flatMap(role -> role.getPermissions().stream().map(Permission::getName))
                .collect(Collectors.toSet());
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(simpleRoles);
        authorizationInfo.setStringPermissions(simplePermissions);
        return authorizationInfo;
    }
}
