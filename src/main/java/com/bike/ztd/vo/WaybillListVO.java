package com.bike.ztd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("WaybillListVO")
public class WaybillListVO {
    @ApiModelProperty("运单ID")
    private String pkId;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户手机号")
    private String userPhone;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("城市")
    private String waybillLocal;
    @ApiModelProperty("收车总数")
    private Integer numberCollect;
    @ApiModelProperty("卸车总数")
    private Integer numberDisboard;
    @ApiModelProperty("收车自行车总数")
    private int numberBicycle = 0;//自行车
    @ApiModelProperty("收车助力车总数")
    private int numberElectric = 0;//助力车
    @ApiModelProperty("自行车编号")
    private List<String> bicycleList;
    @ApiModelProperty("助力车编号")
    private List<String> electricList;
    @ApiModelProperty("照片信息")
    private List<PhotoInfoVO> photoInfoList;
}
