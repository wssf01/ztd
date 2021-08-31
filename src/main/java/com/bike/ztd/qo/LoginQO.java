package com.bike.ztd.qo;

import com.bike.ztd.enums.LoginFromEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("LoginQO")
public class LoginQO {
    @ApiModelProperty("登录来源")
    private LoginFromEnum from;
    @NotNull(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String name;
    @NotNull(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;
}
