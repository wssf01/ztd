package com.bike.ztd.enums;

public enum ExcelType {

    /**
     * 运单列表导出
     */
    WAYBILL_LIST_IMPORT("运车记录导出.xlsx"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    ExcelType(String value) {
        this.value = value;
    }
}
