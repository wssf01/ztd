package com.bike.ztd.enums;

/**
 * 车辆类型
 */
public enum CarTypeEnum {
    BICYCLE("自行车"),
    ELECTRIC("助力车"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    CarTypeEnum(String value) {
        this.value = value;
    }
}
