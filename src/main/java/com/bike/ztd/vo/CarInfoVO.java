package com.bike.ztd.vo;

import com.bike.ztd.enums.CarTypeEnum;
import com.bike.ztd.enums.WaybillInfoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("车辆信息")
public class CarInfoVO {
    @ApiModelProperty("车辆号码")
    private List<String> carNumbers;
    @ApiModelProperty("车辆类型")
    private CarTypeEnum carType;
    @ApiModelProperty("收车/卸车")
    private WaybillInfoEnum type;
}
