package com.bike.ztd.service;

import com.bike.ztd.entity.TWaybillInfo;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TWaybillInfoService extends IService<TWaybillInfo> {

    /**
     * 查询车辆信息
     *
     * @param waybillId
     * @return
     */
    List<TWaybillInfo> getInfoListByWaybillId(String waybillId);

    /**
     * 根据运单id查询该运单收车总数
     *
     * @param id
     * @return
     */
    int getNumberCollectByWaybillId(String id);
}
