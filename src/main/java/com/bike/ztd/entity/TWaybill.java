package com.bike.ztd.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bike.ztd.enums.WaybillEnum;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@TableName("t_waybill")
public class TWaybill extends Model<TWaybill> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("pk_id")
    private String pkId;
    /**
     * 完成时间
     */
    @TableField("complete_at")
    private Date completeAt;
    /**
     * 状态
     */
    @TableField("waybill_status")
    private WaybillEnum waybillStatus;
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
     * 收车总数
     */
    @TableField("number_collect")
    private Integer numberCollect;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 卸车总数
     */
    @TableField("number_disboard")
    private Integer numberDisboard;
    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;
    /**
     * 地点
     */
    @TableField("local_complete")
    private String localComplete;
    /**
     * 经度
     */
    @TableField("longitude_complete")
    private BigDecimal longitudeComplete;
    /**
     * 纬度
     */
    @TableField("latitude_complete")
    private BigDecimal latitudeComplete;
    /**
     * 城市
     */
    private String city;
    /**
     * 完成城市
     */
    @TableField("city_complete")
    private String cityComplete;
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

    public Date getCompleteAt() {
        return completeAt;
    }

    public void setCompleteAt(Date completeAt) {
        this.completeAt = completeAt;
    }

    public WaybillEnum getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(WaybillEnum waybillStatus) {
        this.waybillStatus = waybillStatus;
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

    public Integer getNumberCollect() {
        return numberCollect;
    }

    public void setNumberCollect(Integer numberCollect) {
        this.numberCollect = numberCollect;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getNumberDisboard() {
        return numberDisboard;
    }

    public void setNumberDisboard(Integer numberDisboard) {
        this.numberDisboard = numberDisboard;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocalComplete() {
        return localComplete;
    }

    public void setLocalComplete(String localComplete) {
        this.localComplete = localComplete;
    }

    public BigDecimal getLongitudeComplete() {
        return longitudeComplete;
    }

    public void setLongitudeComplete(BigDecimal longitudeComplete) {
        this.longitudeComplete = longitudeComplete;
    }

    public BigDecimal getLatitudeComplete() {
        return latitudeComplete;
    }

    public void setLatitudeComplete(BigDecimal latitudeComplete) {
        this.latitudeComplete = latitudeComplete;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityComplete() {
        return cityComplete;
    }

    public void setCityComplete(String cityComplete) {
        this.cityComplete = cityComplete;
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
        return "TWaybill{" +
        ", pkId=" + pkId +
        ", completeAt=" + completeAt +
        ", waybillStatus=" + waybillStatus +
        ", waybillLocal=" + waybillLocal +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", numberCollect=" + numberCollect +
        ", remarks=" + remarks +
        ", numberDisboard=" + numberDisboard +
        ", userId=" + userId +
        ", localComplete=" + localComplete +
        ", longitudeComplete=" + longitudeComplete +
        ", latitudeComplete=" + latitudeComplete +
        ", city=" + city +
        ", cityComplete=" + cityComplete +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
