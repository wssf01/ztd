package com.bike.ztd.util;

/**
 * Common层异常枚举
 * Created by d14287 on 2018/4/18.
 */
public enum ErrorEnum implements IError {
    NOT_LOGIN("401", "Unauthorized"),

    UPLOAD_ERROR("upload_error", "upload file error"),

    PERMISSION_LIMITED("permisson_limited", "访问受限"),

    ARGUMENT_NOT_VALID("valid_failed", "参数校验失败"),

    UNVALID_API("unvalid_api", "没有找到对应的API"),

    UNKNOWN_ERROR("unknown_error", "未知错误"),

    OBJECT_NOT_EXSIT("object_not_exist", "对象不存在"),

    PARAMETER_ERROR("PARAMETER_ERROR".toLowerCase(), "传递参数异常"),

    PASSWORD_ERROR("PASSWORD_ERROR".toLowerCase(), "登录名或密码错误"),

    KAPTCHA_EMPTY("kaptcha_empty", "请输入验证码"),

    KAPTCHA_EXPIRED("kaptcha_expired", "验证码已失效，请重新获取"),

    KAPTCHA_ERROR("kaptcha_error", "验证码错误，请重新输入")
    ;

    private String errorCode;
    private String errorMessage;

    private ErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
