package com.bike.ztd.controller;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.bike.ztd.entity.TUser;
import com.bike.ztd.enums.LoginFromEnum;
import com.bike.ztd.enums.UserStatus;
import com.bike.ztd.exception.BusinessException;
import com.bike.ztd.qo.LoginQO;
import com.bike.ztd.service.SendMessageService;
import com.bike.ztd.service.TUserRoleService;
import com.bike.ztd.service.TUserService;
import com.bike.ztd.util.*;
import com.bike.ztd.qo.CellphoneQO;
import com.bike.ztd.vo.LoginVO;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 登录类
 */
@Slf4j
@RestController
@Api(value = "登录")
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private TUserService userService;

    @Autowired
    private TUserRoleService userRoleService;

    @Autowired
    private SendMessageService sendMessageService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${token.expire}")
    private Long expireTime;

    // 失效时间内密码最多错误尝试次数
    @Value("${password.wrong.times}")
    private Integer times;

    // 密码错误限制失效时间（毫秒）
    @Value("${password.wrong.expire}")
    private Integer expire;


    @PostMapping("/login")
    @ApiOperation(value = "登录", response = LoginVO.class)
    public ResponseEntity login(@RequestBody @Valid LoginQO qo) {
        if (loginTimesValidator(qo.getName(), times)) {
            throw new BusinessException("login_error", String.format("连续登录失败超过 %s 次，%s 分钟内不允许再次登录。", times, expire / 1000 / 60));
        }
        try {
            TUser userBean = userService.findUserByLoginName(qo.getName());
            if (userBean == null) {
                log.error("login error:" + qo.getName());
                throw new BusinessException("user_login_error", "帐号密码错误");
            }
            if (UserStatus.DELETE.equals(userBean.getUserStatus())) {
                log.error("login error user delete:" + qo.getName());
                throw new BusinessException("user_delete", "该用户不存在");
            }
            if (UserStatus.DISABLE.equals(userBean.getUserStatus())) {
                log.error("login error user disable:" + qo.getName());
                throw new BusinessException("user_disable", "该用户已被禁用");
            }
            if (LoginFromEnum.ADM.equals(qo.getFrom())) {
                //登录管理端需要判断用户是否有管理员角色
                List<String> role = userRoleService.findRoleIdsByUserId(userBean.getPkId());
                if (!role.contains(SystemDefine.ROLE_ADMIN_ID)) {
                    throw new BusinessException("login_adm_error", "不是管理员无法登录");
                }
            }
            if (userBean.getPassword().equals(qo.getPassword())) {
                LoginVO vo = new LoginVO();
                BeanUtils.copyProperties(userBean, vo);
                vo.setToken(JWTUtils.generateToken(userBean.getPkId(), expireTime));
                log.info("login success:" + qo.getName());
                RedisUtils.delete(String.format(SystemDefine.USER_LOGIN_WRONG_PASSWORD_KEY, qo.getName()));
                return ResponseEntity.success(vo);
            } else {
                log.error("login error:" + qo.getName());
                throw new BusinessException("user_login_error", "帐号密码错误");
            }
        } catch (BusinessException e) {
            log.error(e.getMessage());
            writeLockIncrement(qo.getName(), expire);
            return ResponseEntity.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.failure();
        }
    }

    @GetMapping(value = "/code")
    @ApiOperation(value = "获取短信验证码用于登陆", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cellPhone", value = "手机号")
    })
    public ResponseEntity smsCode(@RequestParam("cellPhone") String cellPhone) {
        //验证手机号合法性
        if (!CheckUtils.isMobilePhone(cellPhone)) {
            throw new BusinessException("user_cellphone_error", "手机号格式错误");
        }
        TUser user = userService.getActiveCellphone(cellPhone);
        if (user == null) {
            throw new BusinessException("sms_login_error", "手机号错误");
        } else {
            if (user.getUserStatus().equals(UserStatus.DELETE)) {
                throw new BusinessException("user_delete", "手机号错误");
            }
            if (user.getUserStatus().equals(UserStatus.DISABLE)) {
                throw new BusinessException("user_disable", "该用户已被禁用");
            }
        }
        final String validKey = String.format(SystemDefine.LOGIN_CELL_PHONE_LOCK, cellPhone);
        if (RedisUtils.get(validKey) != null) {
            throw new BusinessException("active_interval_error", "距离上次发送短信还没到一分钟");
        }
        String code = String.format("%06d", (int) (Math.random() * 1000000));
        String key = String.format("login.cellphone:%s:%s", cellPhone, UUIDUtils.timeBasedStr());
        Map<String, String> map = Maps.newHashMap();
        map.put("cellPhone", cellPhone);
        map.put("code", code);
        // 5分钟验证码失效时间
        RedisUtils.set(key, JSON.toJSONString(map), 60 * 1000 * 5);
        // 1分钟锁定发送时间
        RedisUtils.set(validKey, key, 60 * 1000);
        //发送短信
        try {
            sendMessageService.sendCode(cellPhone, code);
            log.info("get code success :" + cellPhone);
        } catch (BusinessException e) {
            log.error("get code error :" + cellPhone);
            return ResponseEntity.failure(e.getCode(), e.getMsg());
        } catch (ClientException e) {
            log.error("get code error :" + cellPhone);
            return ResponseEntity.failure("send_message_error", "验证码发送失败");
        }
        return ResponseEntity.success(key);
    }

    @PostMapping("/loginCode")
    @ApiOperation(value = "验证码登录", response = LoginVO.class)
    public ResponseEntity smsLogin(@RequestBody @Valid CellphoneQO qo) {
        String json = String.valueOf(RedisUtils.get(qo.getUuid()));
        if (StringUtils.isEmpty(json)) {
            return ResponseEntity.failure("sms_login_error", "验证码无效或验证码已过期，请重新获取验证码");
        }
        if (loginTimesValidator(qo.getCellphone(), times)) {
            throw new BusinessException("login_error", String.format("连续登录失败超过 %s 次，%s 分钟内不允许再次登录。", times, expire / 1000 / 60));
        }
        try {
            Map<String, String> map = (Map<String, String>) JSON.parse(json);
            if (map == null || !StringUtils.equals(qo.getCode(), map.get("code"))) {
                return ResponseEntity.failure("sms_login_error", "验证码无效或验证码已过期，请重新获取验证码");
            }
            TUser user = userService.getActiveCellphone(map.get("cellPhone"));
            if (user == null) {
                throw new BusinessException("sms_login_error", "手机号错误");
            } else {
                if (UserStatus.DELETE.equals(user.getUserStatus())) {
                    throw new BusinessException("user_delete", "手机号错误");
                }
                if (UserStatus.DISABLE.equals(user.getUserStatus())) {
                    throw new BusinessException("user_disable", "该用户已被禁用");
                }
            }
            if (LoginFromEnum.ADM.equals(qo.getFrom())) {
                //登录管理端需要判断用户是否有管理员角色
                List<String> role = userRoleService.findRoleIdsByUserId(user.getPkId());
                if (!role.contains(SystemDefine.ROLE_ADMIN_ID)) {
                    throw new BusinessException("login_adm_error", "不是管理员无法登录");
                }
            }
            LoginVO loginVO = new LoginVO();
            BeanUtils.copyProperties(user, loginVO);
            loginVO.setToken(JWTUtils.generateToken(user.getPkId(), expireTime));
            log.info("login phone success :" + qo.getCellphone());
            // 清除之前错误的内容
            RedisUtils.delete(String.format(SystemDefine.USER_LOGIN_WRONG_PASSWORD_KEY, qo.getCellphone()));
            return ResponseEntity.success(loginVO);
        } catch (BusinessException e) {
            log.error(e.getMessage());
            writeLockIncrement(qo.getCellphone(), expire);
            return ResponseEntity.failure(e.getCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.failure();
        }
    }


    /**
     * 验证登录是否超过错误上限
     *
     * @param loginName
     */
    public static boolean loginTimesValidator(String loginName, int times) {
        long increment = RedisUtils.getIncrValue(String.format(SystemDefine.USER_LOGIN_WRONG_PASSWORD_KEY, loginName));
        if (increment >= times) {
            return true;
        }
        return false;
    }

    /**
     * 累计锁定值
     *
     * @param loginName
     */
    public static void writeLockIncrement(String loginName, int expire) {
        // 增加用户输入密码错误redis
        String key = String.format(SystemDefine.USER_LOGIN_WRONG_PASSWORD_KEY, loginName);
        RedisUtils.increment(key, 1);
        RedisUtils.expire(key, expire); // 刷新过期时间
    }

}
