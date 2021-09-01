package com.bike.ztd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("MyWaybillListVO")
public class MyWaybillListVO {
    @ApiModelProperty("运单id")
    private String id;
    @ApiModelProperty("运单创建时间")
    private Date createTime;
    @ApiModelProperty("收车总数")
    private int numberCollect;
}
