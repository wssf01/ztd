package com.bike.ztd.util;

public class SystemDefine {
    /**
     * 分页查询，分页每页最大值
     */
    public static final int MAX_PAGE_SIZE = 200;

    /**
     * 手机号码一分钟一次获取验证码key
     */
    public static final String LOGIN_CELL_PHONE_LOCK = "login.cellphone.lock:%s";

    /**
     * 用户密码错误过期key
     */
    public static final String USER_LOGIN_WRONG_PASSWORD_KEY = "user.login.wrong.password.%s";

    /**
     * 手机号码获取验证码次数
     */
    public static final String LOGIN_CELL_PHONE_NUM = "login.cellphone.num:%s";

    public static final String ROLE_ADMIN = "admin";

    public static final String ROLE_ADMIN_ID = "1";
}
