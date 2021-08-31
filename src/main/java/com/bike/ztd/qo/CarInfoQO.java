package com.bike.ztd.qo;

import com.bike.ztd.enums.CarTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("CarInfoQO")
public class CarInfoQO {
    @ApiModelProperty("车辆编号")
    private List<String> carNumbers;
    @ApiModelProperty("类型")
    private CarTypeEnum carType;
}
