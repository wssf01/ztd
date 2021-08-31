package com.bike.ztd.enums;

/**
 * 记录类型 收车/卸车
 */
public enum WaybillInfoEnum {
    COLLECT("收车"),
    DISBOARD("卸车"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    WaybillInfoEnum(String value) {
        this.value = value;
    }
}
