package com.bike.ztd.qo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@ApiModel("UserAddQO")
public class UserAddQO {
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
     * 密码
     */
    @ApiModelProperty("密码")
    @Pattern(regexp = "^.*(?=.{6,16})(?=.*\\d)(?=.*[A-Z]{1,})(?=.*[a-z]{1,})(?=.*[_!@#$%^&*?]).*$"
            ,message = "密码需要6-16位，且必须包含数字、小写字母、大写字母、特殊字符")
    private String password = "Aa123!@#";
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
    @NotEmpty(message = "手机号不能为空")
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
    @NotEmpty(message = "姓名不能为空")
    private String userName;
}
