package com.bike.ztd.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("WaybillCompleteQO")
public class WaybillCompleteQO {
    @ApiModelProperty("运单id")
    private String waybillId;
    @ApiModelProperty("备注")
    private String remake;
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("地点")
    private String local;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
}
