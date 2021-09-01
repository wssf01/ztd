package com.bike.ztd.vo;

import com.bike.ztd.enums.WaybillInfoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("PhotoInfoVO")
public class PhotoInfoVO {
    @ApiModelProperty("装车/卸车")
    private WaybillInfoEnum waybillType;
    @ApiModelProperty("照片路径")
    private String photo;
    @ApiModelProperty("地点")
    private String waybillLocal;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
}
