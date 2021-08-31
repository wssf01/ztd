package com.bike.ztd.enums;

/**
 * 订单状态  进行中，已完成，已删除
 */
public enum WaybillEnum {


    DOING("进行中"),
    COMPLETE("已完成"),
    DELETE("已删除"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    WaybillEnum(String value) {
        this.value = value;
    }
}
