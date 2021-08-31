package com.bike.ztd.service;

import com.bike.ztd.entity.TWaybillCar;
import com.baomidou.mybatisplus.service.IService;
import com.bike.ztd.enums.WaybillInfoEnum;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zyp
 * @since 2021-08-31
 */
public interface TWaybillCarService extends IService<TWaybillCar> {

    /**
     * 删除车辆信息
     *
     * @param waybillId
     * @param type
     */
    void deleteByWaybillId(String waybillId, WaybillInfoEnum type);

    /**
     * 根据运单id获取收车总数
     *
     * @param waybillId
     * @return
     */
    int getNumberCollectByWaybillId(String waybillId);

    /**
     * 根据运单id查询收车车辆信息
     *
     * @param id
     * @return
     */
    List<TWaybillCar> getCollectCarListByWaybillId(String id);

    /**
     * 根据运单id查询车辆信息
     *
     * @param waybillId
     * @return
     */
    List<TWaybillCar> getCarListByWaybillId(String waybillId);
}
