package com.bike.ztd.service;

import com.bike.ztd.dto.ImportDTO;
import com.bike.ztd.entity.TExcel;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * excel导出文件表 服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TExcelService extends IService<TExcel> {
    /**
     * 错误数据excel储存
     *
     * @param vo
     */
    void setExcel(ImportDTO vo);
}
