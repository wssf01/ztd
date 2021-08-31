package com.bike.ztd.qo;

import com.bike.ztd.enums.WaybillInfoEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("WaybillInfoAddQO")
public class WaybillInfoAddQO {
    @ApiModelProperty("运单id")
    private String waybillId;
    @ApiModelProperty("动作类型")
    private WaybillInfoEnum type;
    @ApiModelProperty("车辆信息")
    private List<CarInfoQO> carInfoList;
    @ApiModelProperty("图片信息")
    private List<PhotoInfoQO> photoList;
}
