package com.bike.ztd.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("UserImportTotalDTO")
public class UserImportTotalDTO implements Serializable {

    private static final long serialVersionUID = -1816696216897853467L;

    @ApiModelProperty("总数")
    private int total;

    @ApiModelProperty("成功数")
    private int success;

    @ApiModelProperty("失败数")
    private int failure;

    // 剩余个数
    @ApiModelProperty("剩余数")
    private int restCount;

    @ApiModelProperty("结果集")
    private transient List<UserImportResultDTO> failureNotes;

    public static UserImportTotalDTO build(int total) {
        UserImportTotalDTO dto = new UserImportTotalDTO();
        dto.setTotal(total);
        dto.setFailure(0);
        dto.setRestCount(total);
        dto.setSuccess(0);
        return dto;
    }

    private static final String FORMAT = "导入总条数: %s, 成功条数: %s";

    public String result() {
        return String.format(FORMAT, total, success);
    }
}
