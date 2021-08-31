package com.bike.ztd.enums;

public enum UserStatus {

    NORMAL("正常"),
    DISABLE("禁用"),
    DELETE("删除"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    UserStatus(String value) {
        this.value = value;
    }
}
