package com.bike.ztd.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("PhotoInfoQO")
public class PhotoInfoQO {
    @ApiModelProperty("照片路径")
    private String photo;
    @ApiModelProperty("地点")
    private String local;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
}
