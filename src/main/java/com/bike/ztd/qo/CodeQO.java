package com.bike.ztd.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("CodeQO")
public class CodeQO {
    @ApiModelProperty("手机号")
    private String phone;
    @ApiModelProperty("唯一标识")
    private String uuid;
}
