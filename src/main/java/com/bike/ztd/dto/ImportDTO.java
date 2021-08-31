package com.bike.ztd.dto;

import com.bike.ztd.enums.ExcelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel("ImportDTO")
public class ImportDTO {

    @ApiModelProperty("错误数据插入表中的id")
    private String modelId;
    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("用户id")
    private String userId;
    @ApiModelProperty("用户名")
    private String userName;
    @ApiModelProperty("数据类型")
    private ExcelType type;
    @ApiModelProperty("输出模型")
    private Map<String, Object> model;
    private String key;
}
