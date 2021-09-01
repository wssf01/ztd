package com.bike.ztd.vo;

import com.baomidou.mybatisplus.annotations.TableField;
import com.bike.ztd.entity.TWaybillInfo;
import com.bike.ztd.enums.WaybillEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@ApiModel("WaybillDetailsVO")
public class WaybillDetailsVO {
    @ApiModelProperty("运单id")
    private String id;
    @ApiModelProperty("完成时间")
    private Date completeAt;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("运单状态")
    private WaybillEnum waybillStatus;
    @ApiModelProperty("地点")
    private String waybillLocal;
    @ApiModelProperty("经度")
    private BigDecimal longitude;
    @ApiModelProperty("纬度")
    private BigDecimal latitude;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("完成地点")
    private String localComplete;
    @ApiModelProperty("完成经度")
    private BigDecimal longitudeComplete;
    @ApiModelProperty("完成纬度")
    private BigDecimal latitudeComplete;
    @ApiModelProperty("收集的自行车总数")
    private int collectBicycle = 0;
    @ApiModelProperty("收集的助力车总数")
    private int collectElectric = 0;
    @ApiModelProperty("收车照片信息")
    private List<PhotoInfoVO> collectList;
    @ApiModelProperty("卸车的自行车总数")
    private int disboardBicycle = 0;
    @ApiModelProperty("卸车的助力车总数")
    private int disboardElectric = 0;
    @ApiModelProperty("卸车照片信息")
    private List<PhotoInfoVO> disdoardList;
    @ApiModelProperty("车辆号码")
    private List<CarInfoVO> carInfoList;
}
