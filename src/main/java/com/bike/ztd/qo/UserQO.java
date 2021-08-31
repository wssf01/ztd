package com.bike.ztd.qo;

import com.bike.ztd.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("UserQO")
public class UserQO {
    /**
     * 主键ID
     */
    @ApiModelProperty("用户id")
    private String id;
    /**
     * 年龄
     */
    @ApiModelProperty("年龄")
    private Integer age;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private Integer gender;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 手机
     */
    @ApiModelProperty("手机")
    private String phone;
    /**
     * 身份证
     */
    @ApiModelProperty("身份证")
    private String identityCard;
    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String userName;
    /**
     * 用户状态
     */
    @ApiModelProperty("用户状态")
    private UserStatus status;
}
