package com.bike.ztd.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bike.ztd.enums.CarTypeEnum;
import com.bike.ztd.enums.WaybillInfoEnum;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@TableName("t_waybill_car")
public class TWaybillCar extends Model<TWaybillCar> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("pk_id")
    private String pkId;
    /**
     * 收车/装车
     */
    @TableField("waybill_type")
    private WaybillInfoEnum waybillType;
    /**
     * 运单id
     */
    @TableField("waybill_id")
    private String waybillId;
    /**
     * 车辆编号
     */
    @TableField("car_number")
    private String carNumber;
    /**
     * 车辆类型
     */
    @TableField("car_type")
    private CarTypeEnum carType;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;


    public String getPkId() {
        return pkId;
    }

    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    public WaybillInfoEnum getWaybillType() {
        return waybillType;
    }

    public void setWaybillType(WaybillInfoEnum waybillType) {
        this.waybillType = waybillType;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public CarTypeEnum getCarType() {
        return carType;
    }

    public void setCarType(CarTypeEnum carType) {
        this.carType = carType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.pkId;
    }

    @Override
    public String toString() {
        return "TWaybillCar{" +
        ", pkId=" + pkId +
        ", waybillType=" + waybillType +
        ", waybillId=" + waybillId +
        ", carNumber=" + carNumber +
        ", carType=" + carType +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
