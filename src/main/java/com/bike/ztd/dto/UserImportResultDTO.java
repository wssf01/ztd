package com.bike.ztd.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class UserImportResultDTO implements Serializable {
    private static final long serialVersionUID = 204763338949272841L;
    @ApiModelProperty("用户名称")
    private String name;
    @ApiModelProperty("用户手机号")
    private String phone;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("序号")
    private int index;

    public static UserImportResultDTO build(UserImportDTO dto, String note, int index) {
        UserImportResultDTO resultDTO = new UserImportResultDTO();
        resultDTO.setPhone(dto.getPhone());
        resultDTO.setName(dto.getName());
        resultDTO.setNote(note.substring(1));
        resultDTO.setIndex(index);
        return resultDTO;
    }
}
