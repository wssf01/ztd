package com.bike.ztd.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("LoginVO")
public class LoginVO {
    /**
     * 主键ID
     */
    @ApiModelProperty("用户id")
    private String id;
    /**
     * 用户名
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
     * token
     */
    @ApiModelProperty("token")
    private String token;
}
