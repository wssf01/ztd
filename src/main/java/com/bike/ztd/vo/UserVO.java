package com.bike.ztd.vo;

import com.bike.ztd.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("UserVO")
public class UserVO {
    /**
     * 主键ID
     */
    @ApiModelProperty("用户id")
    private String id;
    /**
     * 登录名
     */
    @ApiModelProperty("登录名")
    private String loginName;
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
    @ApiModelProperty("身份证")
    private String identityCard;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("用户姓名")
    private String userName;
    @ApiModelProperty("用户状态")
    private UserStatus status;
}
