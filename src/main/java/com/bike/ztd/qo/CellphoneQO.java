package com.bike.ztd.qo;

import com.bike.ztd.enums.LoginFromEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("CellphoneQO")
public class CellphoneQO {
    @ApiModelProperty("登录来源")
    private LoginFromEnum from;

    @ApiModelProperty("用户手机号, 主要防止手机号发生变更")
    private String cellphone;

    @NotBlank
    @ApiModelProperty("批次号:code接口返回的值")
    private String uuid;

    @NotBlank
    @ApiModelProperty("验证码")
    private String code;
}
