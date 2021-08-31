package com.bike.ztd.exception;


import com.bike.ztd.util.IError;

public class BusinessException extends RuntimeException {

    private String code;
    private String msg;

    private IError error;

    // 增加错误参数附加内容
    private Object object;

    public BusinessException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusinessException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String code, String msg, Object object) {
        this(code, msg);
        this.object = object;
    }

    public BusinessException(IError error) {
        super(error.getErrorMessage());
        this.error = error;
        this.code = error.getErrorCode();
        this.msg = error.getErrorMessage();
    }

    public IError getError() {
        return error;
    }

    public void setError(IError error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObject() {
        return object;
    }

    public BusinessException setObject(Object object) {
        this.object = object;
        return this;
    }
}
