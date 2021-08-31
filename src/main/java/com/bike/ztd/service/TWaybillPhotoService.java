package com.bike.ztd.service;

import com.bike.ztd.entity.TWaybillPhoto;
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
public interface TWaybillPhotoService extends IService<TWaybillPhoto> {

    /**
     * 删除照片
     *
     * @param waybillId
     * @param type
     */
    void deleteByWaybillId(String waybillId, WaybillInfoEnum type);

    /**
     * 根据运单id查找照片
     *
     * @param waybillId
     * @return
     */
    List<TWaybillPhoto> selectListByWaybillId(String waybillId);
}
