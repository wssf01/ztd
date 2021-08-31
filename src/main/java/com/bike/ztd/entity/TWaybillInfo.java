package com.bike.ztd.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@TableName("t_waybill_info")
public class TWaybillInfo extends Model<TWaybillInfo> {

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
    private String waybillType;
    /**
     * 地点
     */
    @TableField("waybill_local")
    private String waybillLocal;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 图片路径
     */
    private String photo;
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
    @TableField("car_type")
    private String carType;
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

    public String getWaybillType() {
        return waybillType;
    }

    public void setWaybillType(String waybillType) {
        this.waybillType = waybillType;
    }

    public String getWaybillLocal() {
        return waybillLocal;
    }

    public void setWaybillLocal(String waybillLocal) {
        this.waybillLocal = waybillLocal;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
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
        return "TWaybillInfo{" +
        ", pkId=" + pkId +
        ", waybillType=" + waybillType +
        ", waybillLocal=" + waybillLocal +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", photo=" + photo +
        ", waybillId=" + waybillId +
        ", carNumber=" + carNumber +
        ", carType=" + carType +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
