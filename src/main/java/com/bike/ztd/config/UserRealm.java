package com.bike.ztd.config;

import com.bike.ztd.entity.TUser;
import com.bike.ztd.enums.UserStatus;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.service.*;
import com.bike.ztd.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * 用户权限realm ，认证授权最终都在此处实现，用于安全框架dao
 */
@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private TUserService userService;

    @Autowired
    private TRoleService roleService;

    @Autowired
    private TUserRoleService userRoleService;

    @Autowired
    private TRoleMenuService roleMenuService;

    @Autowired
    private TMenuService menuService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 执行认证逻辑
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        System.out.println("UserRealm.doGetAuthenticationInfo  认证");
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String userId = JWTUtils.getUserIdFromToken(token);
        if (userId == null) {
            throw new AuthenticationException("token invalid");
        }
        if (!JWTUtils.verify(token, userId)) {
            throw new AuthenticationException("token invalid");
        }
        TUser user = userService.findUserByUserId(userId);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        } else {
            if (UserStatus.DELETE.equals(user.getUserStatus())) {
                throw new AuthenticationException("user_delete");
            }
            if (UserStatus.DISABLE.equals(user.getUserStatus())) {
                throw new AuthenticationException("user_disable");
            }
        }
        return new SimpleAuthenticationInfo(token, token, "user_realm");
    }

    /**
     * 执行赋予授权逻辑
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("UserRealm.doGetAuthorizationInfo  授权");
        String userId = JWTUtils.getUserIdFromToken(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //1.通过用户id查找出用户角色
        List<String> roleIdsList = userRoleService.findRoleIdsByUserId(userId);
        List<String> roleNamesList = roleService.findRoleNameByRoleIds(roleIdsList);
        HashSet<String> roleSet = new HashSet<>(roleNamesList);
        info.setRoles(roleSet);
        //2.通过角色找到对应的权限
        HashSet<String> roleIdsSet = new HashSet<>(roleIdsList);
        List<String> menuIds = roleMenuService.findMenuIdsByRoleIds(roleIdsSet);
        List<String> perms = menuService.findPermsByMenuIds(menuIds);
        info.addStringPermissions(perms);
        return info;
    }
}
