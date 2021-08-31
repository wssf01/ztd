package com.bike.ztd.dto;

import lombok.Data;


@Data
public class WaybillExportDTO {
    /**
     * 城市
     */
    private String city;

    /**
     * 用户手机号
     */
    private String userPhone;
    /**
     * 创建时间 20210627
     */
    private String createdAt;

    /**
     * 收车总数
     */
    private Integer numberCollect;

    /**
     * 卸车总数
     */
    private Integer numberDisboard;

    /**
     * 车辆编号
     */
    private String bicycleList;
    private String electricList;

    /**
     * 收车地址
     */
    private String local;

    /**
     * 收车经纬度
     */
    private String situation;

    /**
     * 投车地址
     */
    private String outLocal;

    /**
     * 投车经纬度
     */
    private String outSituation;

}
