package com.bike.ztd.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.bike.ztd.enums.ExcelType;

import java.io.Serializable;

/**
 * <p>
 * excel导出文件表
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
@TableName("t_excel")
public class TExcel extends Model<TExcel> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId("pk_id")
    private String pkId;
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    /**
     * 内容
     */
    @TableField("excel_content")
    private String excelContent;
    /**
     * oss存放文件路径
     */
    @TableField("oss_path")
    private String ossPath;
    /**
     * 模版文件名
     */
    @TableField("excel_type")
    private ExcelType excelType;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExcelContent() {
        return excelContent;
    }

    public void setExcelContent(String excelContent) {
        this.excelContent = excelContent;
    }

    public String getOssPath() {
        return ossPath;
    }

    public void setOssPath(String ossPath) {
        this.ossPath = ossPath;
    }

    public ExcelType getExcelType() {
        return excelType;
    }

    public void setExcelType(ExcelType excelType) {
        this.excelType = excelType;
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
        return "TExcel{" +
        ", pkId=" + pkId +
        ", fileName=" + fileName +
        ", excelContent=" + excelContent +
        ", ossPath=" + ossPath +
        ", excelType=" + excelType +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
